package com.ms_ma_backend_test.ms_ma.factory;

import com.ms_ma_backend_test.ms_ma.dtos.SignUpRequest;
import com.ms_ma_backend_test.ms_ma.entity.User;

public class Factory {

    public static SignUpRequest buildSignUpRequestValid() {

        SignUpRequest request = new SignUpRequest("username", User.ProfileVisibility.PUBLIC, "123456asdfA");
        return request;
    }

}
