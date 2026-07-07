package com.codewithben.Lofau.group.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.entity.GroupMember;
import com.codewithben.Lofau.group.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

    Optional<GroupMember> findByGroupAndUser(
            Group group,
            User user
    );

    Optional<GroupMember> findByGroupIdAndUserId(
            UUID groupId,
            Long userId
    );

    boolean existsByGroupAndUser(
            Group group,
            User user
    );

    boolean existsByGroupIdAndUserId(
            UUID groupId,
            Long userId
    );

    List<GroupMember> findByGroup(
            Group group
    );

    List<GroupMember> findByUser(
            User user
    );

    List<GroupMember> findByGroupAndStatus(
            Group group,
            MemberStatus status
    );

    List<GroupMember> findByGroupIdAndStatus(
            UUID groupId,
            MemberStatus status
    );

    long countByGroup(
            Group group
    );

    long countByGroupAndStatus(
            Group group,
            MemberStatus status
    );

    void deleteByGroupAndUser(
            Group group,
            User user
    );

    void deleteByGroupIdAndUserId(
            UUID groupId,
            Long userId
    );

}