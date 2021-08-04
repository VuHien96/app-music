package com.vuhien.application.repository;

import com.vuhien.application.constant.RoleName;
import com.vuhien.application.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by VuHien96 on 02/08/2021 15:50
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    //1.Lấy role theo roleName
    Optional<Role> findByRoleName(RoleName roleName);

}
