package com.vuhien.application.service.impl;

import com.vuhien.application.constant.RoleName;
import com.vuhien.application.entity.Role;
import com.vuhien.application.entity.User;
import com.vuhien.application.exception.BadRequestException;
import com.vuhien.application.exception.NotFoundException;
import com.vuhien.application.model.request.RegisterRequest;
import com.vuhien.application.repository.RoleRepository;
import com.vuhien.application.repository.UserRepository;
import com.vuhien.application.service.EmailSenderService;
import com.vuhien.application.service.UserService;
import com.vuhien.application.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by VuHien96 on 02/08/2021 15:53
 */
@Component
public class UserServiceImpl implements UserService {
    /*
        1.Kiểm tra email có tồn tại không
        2.Kiểm tra quyền của
        3.Lưu user vào database
        4.Tạo mã token
        5.Tạo đường link
        6.Gửi email kèm đường link
     */

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public User createUser(RegisterRequest registerRequest) {
        Optional<User> rs = userRepository.findByEmail(registerRequest.getEmail());
        if (rs.isPresent()) {
            throw new BadRequestException("Email đã tồn tại");
        }
        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        String hash = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(false);
        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN).orElseThrow(() -> new NotFoundException("Role admin not found"));
                    roles.add(roleAdmin);
                    break;
                case "pm":
                    Role rolePm = roleRepository.findByRoleName(RoleName.PM).orElseThrow(() -> new NotFoundException("Role pm not found"));
                    roles.add(rolePm);
                    break;
                default:
                    Role roleUser = roleRepository.findByRoleName(RoleName.USER).orElseThrow(() -> new NotFoundException("Role user not found"));
                    roles.add(roleUser);
                    break;
            }
        });
        user.setRoles(roles);
        userRepository.save(user);
        String token = verificationTokenService.createVerificationToken(user);
        String link = "https://music-vh.herokuapp.com/api/users/confirm?token=" + token;
        emailSenderService.sendEmail(registerRequest.getEmail(), registerRequest.getFullName(), link);
        return user;
    }

}
