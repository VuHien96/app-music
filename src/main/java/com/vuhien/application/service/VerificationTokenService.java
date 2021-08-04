package com.vuhien.application.service;

import com.vuhien.application.entity.User;
import org.springframework.stereotype.Service;

/**
 * Created by VuHien96 on 02/08/2021 15:53
 */
@Service
public interface VerificationTokenService {
    String createVerificationToken(User user);
    void confirmVerificationToken(String token);
}
