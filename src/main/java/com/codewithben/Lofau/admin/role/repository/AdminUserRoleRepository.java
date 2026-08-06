package com.codewithben.Lofau.admin.role.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.admin.role.entity.AdminRole;
import com.codewithben.Lofau.admin.role.entity.AdminUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdminUserRoleRepository
        extends JpaRepository<AdminUserRole, UUID> {

    List<AdminUserRole> findByUser(User user);

    List<AdminUserRole> findByUserAndActiveTrue(User user);

    boolean existsByUserAndRole(User user,
                                AdminRole role);

    void deleteByUserAndRole(User user,
                             AdminRole role);

    long countByRole(AdminRole role);

    List<AdminUserRole> findAllByActiveTrue();

    List<AdminUserRole> findAllByRole(AdminRole role);
    List<AdminUserRole> findByActiveTrue();

    List<AdminUserRole> findByRole(AdminRole role);

    List<AdminUserRole> findByRoleAndActiveTrue(AdminRole role);

}