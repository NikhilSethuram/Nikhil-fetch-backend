package com.fetch.backendproject.service;

import com.fetch.backendproject.model.PayersModel;
import com.fetch.backendproject.model.UserModel;
import com.fetch.backendproject.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void insertUser(UserModel userModel) {
        UserModel model = new UserModel();
        BeanUtils.copyProperties(userModel,model);
        userRepository.insertUserInfo(model);
    }

    public Map<String, Integer> spendPoints(int points) {
        try {
            Map<String, Integer> mymap=  userRepository.spendUserPoints(points);
            return mymap;
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public List<PayersModel> getPayersofUser(){
        return userRepository.getPayers();
    }
}
