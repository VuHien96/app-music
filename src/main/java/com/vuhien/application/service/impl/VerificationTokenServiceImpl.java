package com.vuhien.application.service.impl;

import com.vuhien.application.entity.User;
import com.vuhien.application.entity.VerificationToken;
import com.vuhien.application.exception.BadRequestException;
import com.vuhien.application.exception.NotFoundException;
import com.vuhien.application.repository.UserRepository;
import com.vuhien.application.repository.VerificationTokenRepository;
import com.vuhien.application.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by VuHien96 on 02/08/2021 15:53
 */
@Component
public class VerificationTokenServiceImpl implements VerificationTokenService {
    /*
        1.Tạo token
        2.Tạo hàm confirmToken
            - Kiểm tra mã có tồn tại không
            - Kiểm tra thời gian xác nhận có != null không
            - Kiểm tra hạn của token đã hết hạn chưa
            - Cập nhật trạng thái của user
            - Cập nhật thời gian confirm
     */

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        String token = UUID.randomUUID().toString();
        verificationToken.setToken(token);
        verificationToken.setCreatedAt(LocalDateTime.now());
        verificationToken.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Override
    public void confirmVerificationToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.isEmpty()) {
            throw new NotFoundException("Mã không tồn tại");
        }
        if (verificationToken.get().getConfirmedAt() != null) {
            throw new BadRequestException("Tài khoàn đã kích hoạt");
        }
        LocalDateTime expiredAt = verificationToken.get().getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã đã hết hạn");
        }
        userRepository.updateStatusUser(verificationToken.get().getUser().getEmail());
        verificationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}
