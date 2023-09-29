package com.fetch.backendproject.controller;

import com.fetch.backendproject.model.PayersModel;
import com.fetch.backendproject.model.SpendModel;
import com.fetch.backendproject.model.UserModel;
import com.fetch.backendproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/add")
    public ResponseEntity<Void> addUserData(@RequestBody UserModel user){
        service.insertUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/spend")
    public ResponseEntity<Object> spendUserPoints(@RequestBody SpendModel spendmodel) {
        Map<String, Integer> mymap = service.spendPoints(spendmodel.getPoints());

        if (mymap == null) {
            return ResponseEntity
                    .badRequest()
                    .body("User doesn't have enough points");
        } else {
            return ResponseEntity.ok(mymap);
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<Object> getUserBalance(){
        List<PayersModel> mylist= service.getPayersofUser();
        return ResponseEntity.ok(mylist);
    }

}
