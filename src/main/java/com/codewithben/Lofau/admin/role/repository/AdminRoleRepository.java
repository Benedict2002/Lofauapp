package com.codewithben.Lofau.admin.role.repository;

import com.codewithben.Lofau.admin.role.entity.AdminRole;
import com.codewithben.Lofau.admin.role.enums.AdminRoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdminRoleRepository extends JpaRepository<AdminRole, UUID> {

    Optional<AdminRole> findByName(AdminRoleName name);

    boolean existsByName(AdminRoleName name);

    List<AdminRole> findAllByOrderByNameAsc();

}