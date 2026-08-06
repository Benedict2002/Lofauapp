package com.codewithben.Lofau.admin.audit.specification;

import com.codewithben.Lofau.admin.audit.dto.request.AuditFilterRequest;
import com.codewithben.Lofau.admin.audit.entity.AuditLog;
import org.springframework.data.jpa.domain.Specification;

public class AuditSpecification {

    private AuditSpecification() {
    }

    /**
     * ==========================================================
     * Build dynamic filters for Audit Logs.
     * ==========================================================
     */
    public static Specification<AuditLog> filter(
            AuditFilterRequest request
    ) {

        return Specification.where(admin(request))
                .and(action(request))
                .and(entityType(request))
                .and(startDate(request))
                .and(endDate(request));

    }

    /**
     * ==========================================================
     * Filter by Admin
     * ==========================================================
     */
    private static Specification<AuditLog> admin(
            AuditFilterRequest request
    ) {

        return (root, query, cb) -> {

            if (request.getAdminId() == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("admin").get("id"),
                    request.getAdminId()
            );
        };
    }

    /**
     * ==========================================================
     * Filter by Action
     * ==========================================================
     */
    private static Specification<AuditLog> action(
            AuditFilterRequest request
    ) {

        return (root, query, cb) -> {

            if (request.getAction() == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("action"),
                    request.getAction()
            );
        };
    }

    /**
     * ==========================================================
     * Filter by Entity Type
     * ==========================================================
     */
    private static Specification<AuditLog> entityType(
            AuditFilterRequest request
    ) {

        return (root, query, cb) -> {

            if (request.getEntityType() == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("entityType"),
                    request.getEntityType()
            );
        };
    }

    /**
     * ==========================================================
     * Created After
     * ==========================================================
     */
    private static Specification<AuditLog> startDate(
            AuditFilterRequest request
    ) {

        return (root, query, cb) -> {

            if (request.getStartDate() == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(
                    root.get("createdAt"),
                    request.getStartDate()
            );
        };
    }

    /**
     * ==========================================================
     * Created Before
     * ==========================================================
     */
    private static Specification<AuditLog> endDate(
            AuditFilterRequest request
    ) {

        return (root, query, cb) -> {

            if (request.getEndDate() == null) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(
                    root.get("createdAt"),
                    request.getEndDate()
            );
        };
    }

}