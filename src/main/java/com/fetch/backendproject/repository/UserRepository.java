package com.fetch.backendproject.repository;

import com.fetch.backendproject.Tables;
import com.fetch.backendproject.model.PayersModel;
import com.fetch.backendproject.model.UserModel;
import com.fetch.backendproject.tables.pojos.User;
import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserRepository {
    @Autowired
    private DSLContext dslContext;

    public void insertUserInfo(UserModel userModel) {
        dslContext.insertInto(Tables.USER, Tables.USER.PAYER, Tables.USER.POINTS, Tables.USER.TIMESTAMP)
                .values( userModel.getPayer(), userModel.getPoints(),userModel.getTimestamp())
                .execute();
    }

    public Map<String, Integer> spendUserPoints(int points) {
        // Find points that were added earliest using timestamp and use them
        Result<Record3<String, Integer, LocalDateTime>> records = dslContext.select(
                        Tables.USER.PAYER,
                        Tables.USER.POINTS,
                        Tables.USER.TIMESTAMP)
                .from(Tables.USER)
                .orderBy(Tables.USER.TIMESTAMP)
                .limit(points)
                .fetch();

        // Calculate the total points that can be spent
        int totalPoints = records.stream()
                .map(Record3::component2)
                .mapToInt(Integer::intValue)
                .sum();

        if (totalPoints < points) {
            // Not enough points to spend
            throw new IllegalArgumentException("User doesn't have enough points");
        }

        // User has enough points, points were deducted from each payer
        Map<String, Integer> deductions = new HashMap<>();
        int remainingPoints = points;

        for (Record3<String, Integer, LocalDateTime> record : records) {
            String payer = record.component1();
            int payerPoints = record.component2();
            LocalDateTime timestamp = record.component3();

            int deductedPoints = Math.min(payerPoints, remainingPoints);
            deductions.put(payer, deductions.getOrDefault(payer, 0) + deductedPoints);
            remainingPoints -= deductedPoints;

            if (remainingPoints == 0) {
                break;
            }
        }

        // Deduct points from the database
        for (Map.Entry<String, Integer> entry : deductions.entrySet()) {
            String payer = entry.getKey();
            int deductedPoints = entry.getValue();

            dslContext.update(Tables.USER)
                    .set(Tables.USER.POINTS, Tables.USER.POINTS.minus(deductedPoints))
                    .where(Tables.USER.PAYER.eq(payer))
                    .and(Tables.USER.TIMESTAMP.eq(
                            DSL.select(Tables.USER.TIMESTAMP)
                                    .from(Tables.USER)
                                    .where(Tables.USER.PAYER.eq(payer))
                                    .orderBy(Tables.USER.TIMESTAMP.asc())
                                    .limit(1)
                    ))
                    .execute();
        }

        return deductions;
    }

    public List<PayersModel> getPayers() {
        List<User> userlist = dslContext.selectFrom(Tables.USER).fetchInto(User.class);

        Map<String, Integer> payerPointsMap = userlist.stream()
                .collect(Collectors.groupingBy(User::getPayer, Collectors.summingInt(User::getPoints)));

        List<PayersModel> payersList = payerPointsMap.entrySet().stream()
                .map(entry -> new PayersModel(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return payersList;
    }

}
