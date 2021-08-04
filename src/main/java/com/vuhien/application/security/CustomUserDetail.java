package com.vuhien.application.security;

import com.vuhien.application.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by VuHien96 on 02/08/2021 22:34
 */
@NoArgsConstructor
@Setter
@Getter
public class CustomUserDetail implements UserDetails {
    /*
        1.Tạo thêm các thuộc tính cho user
     */
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private Boolean status;
    private Collection<? extends GrantedAuthority> roles;

    public CustomUserDetail(Long id, String fullName, String email, String password, String phone, Boolean status, Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.status = status;
        this.roles = roles;
    }

    public static CustomUserDetail build(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        return new CustomUserDetail(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.isStatus(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}
