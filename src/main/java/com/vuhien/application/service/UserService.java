package com.vuhien.application.service;

import com.vuhien.application.entity.User;
import com.vuhien.application.model.request.RegisterRequest;
import org.springframework.stereotype.Service;

/**
 * Created by VuHien96 on 02/08/2021 15:52
 */
@Service
public interface UserService {
    User createUser(RegisterRequest registerRequest);
}
