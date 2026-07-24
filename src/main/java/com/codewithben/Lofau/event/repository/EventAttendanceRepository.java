package com.codewithben.Lofau.event.repository;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.event.entity.Event;
import com.codewithben.Lofau.event.entity.EventAttendance;
import com.codewithben.Lofau.event.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventAttendanceRepository
        extends JpaRepository<EventAttendance, UUID> {

    boolean existsByEventAndUser(
            Event event,
            User user
    );

    Optional<EventAttendance> findByEventAndUser(
            Event event,
            User user
    );

    List<EventAttendance> findByEvent(
            Event event
    );

    List<EventAttendance> findByUser(
            User user
    );

    List<EventAttendance> findTop3ByEventAndStatusOrderByIdAsc(
            Event event,
            AttendanceStatus status
    );

    long countByEventAndStatus(
            Event event,
            AttendanceStatus status
    );

    void deleteByEventAndUser(
            Event event,
            User user
    );
}