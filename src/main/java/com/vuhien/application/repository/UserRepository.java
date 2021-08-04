package com.vuhien.application.repository;

import com.vuhien.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by VuHien96 on 02/08/2021 15:50
 */
@Transactional(readOnly = true)
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //1.Lấy user theo email;
    Optional<User> findByEmail(String email);

    //2.Cập nhật lại trạng thái của user theo email;
    @Modifying
    @Transactional
    @Query("UPDATE User u " +
            "SET u.status = true " +
            "WHERE u.email = ?1")
    void updateStatusUser(String email);
}
