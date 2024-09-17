package com.ms_ma_backend_test.ms_ma.controller;

import com.ms_ma_backend_test.ms_ma.dtos.LoginRequest;
import com.ms_ma_backend_test.ms_ma.dtos.LoginResponse;
import com.ms_ma_backend_test.ms_ma.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var response = tokenService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
