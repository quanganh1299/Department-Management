package com.vti.service;

import com.vti.entity.Account;

public interface IResetPasswordService {

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);
}
