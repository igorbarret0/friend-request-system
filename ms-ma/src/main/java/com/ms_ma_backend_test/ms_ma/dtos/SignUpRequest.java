package com.ms_ma_backend_test.ms_ma.dtos;

import com.ms_ma_backend_test.ms_ma.entity.User;

public record SignUpRequest(
        String username,
        User.ProfileVisibility profileVisibility,
        String password
) {
}
