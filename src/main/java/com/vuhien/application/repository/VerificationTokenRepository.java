package com.vuhien.application.repository;

import com.vuhien.application.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by VuHien96 on 02/08/2021 15:50
 */
@Transactional(readOnly = true)
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    //1.Lấy VerificationToken theo token
    Optional<VerificationToken> findByToken(String token);

    //2. Cập nhật lại confirmedAt theo mã token
    @Modifying
    @Transactional
    @Query("UPDATE VerificationToken v " +
            "SET v.confirmedAt = ?2 " +
            "WHERE v.token = ?1 ")
    void updateConfirmedAt(String token, LocalDateTime confirmedAt);
}
