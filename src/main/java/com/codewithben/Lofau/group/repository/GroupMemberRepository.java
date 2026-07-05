package com.codewithben.Lofau.group.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.group.entity.Group;
import com.codewithben.Lofau.group.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    List<GroupMember> findByGroup(Group group);

    List<GroupMember> findByUser(User user);

    boolean existsByGroupAndUser(Group group, User user);

}