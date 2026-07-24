package com.codewithben.Lofau.event.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.event.entity.Event;
import com.codewithben.Lofau.group.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository
        extends JpaRepository<Event, UUID> {

    Page<Event> findByGroup(
            Group group,
            Pageable pageable
    );

    Page<Event> findByCreatedBy(
            User createdBy,
            Pageable pageable
    );

    Page<Event> findByActiveTrue(
            Pageable pageable
    );
}