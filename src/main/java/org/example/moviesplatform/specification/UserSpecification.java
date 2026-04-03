package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.example.moviesplatform.model.UserFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<UserEntity> getSpecification(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) return cb.conjunction();

            if (filter.getUsername() != null && !filter.getUsername().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("username").as(String.class)),
                        "%" + filter.getUsername().toLowerCase().trim() + "%"
                ));
            }

            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("email").as(String.class)),
                        "%" + filter.getEmail().toLowerCase().trim() + "%"
                ));
            }

            if (filter.getCreatedAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtFrom()));
            }

            if (filter.getCreatedAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtTo()));
            }

            if (filter.getIncludeDeleted() != null && !filter.getIncludeDeleted()) {
                predicates.add(cb.equal(root.get("isDeleted"), false));
            }

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}