package com.ms_ma_backend_test.ms_ma.controller;

import com.ms_ma_backend_test.ms_ma.dtos.SignUpRequest;
import com.ms_ma_backend_test.ms_ma.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest request) {

        userService.signUp(request);
        return ResponseEntity.ok("User signed up successfully");
    }
}
