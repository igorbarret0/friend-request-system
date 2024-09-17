package com.ms_ma_backend_test.ms_ma.dtos;

public record LoginResponse(
        String accessToken,
        Long expiresIn
) {
}
