package com.codewithben.Lofau.User.controller;

import com.codewithben.Lofau.User.dao.response.LofauResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lofau")
public class HomeController {


    @GetMapping
    public ResponseEntity<LofauResponse> homeController(){
        LofauResponse res = new LofauResponse();
        res.setDescription("Welcome to Lofau Application. The app is up and Running smoothly");
        return  new ResponseEntity<>(res,HttpStatus.OK );
    }
}
