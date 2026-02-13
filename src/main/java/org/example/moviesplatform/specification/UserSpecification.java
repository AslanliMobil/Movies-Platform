package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.Predicate;
// 1. DÜZƏLİŞ: Köhnə User yerinə UserEntity import edildi
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.example.moviesplatform.model.UserFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    // 2. DÜZƏLİŞ: Specification<User> -> Specification<UserEntity> edildi
    public static Specification<UserEntity> getSpecification(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) return cb.conjunction();

            // 1. Username
            if (filter.getUsername() != null && !filter.getUsername().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("username").as(String.class)),
                        "%" + filter.getUsername().toLowerCase().trim() + "%"
                ));
            }

            // 2. Email
            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("email").as(String.class)),
                        "%" + filter.getEmail().toLowerCase().trim() + "%"
                ));
            }

            // 3. Tarix Aralığı
            if (filter.getCreatedAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtFrom()));
            }

            if (filter.getCreatedAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtTo()));
            }

            // 4. Soft Delete (isDeleted sahəsinin UserEntity-də olduğundan əmin ol)
            if (filter.getIncludeDeleted() != null && !filter.getIncludeDeleted()) {
                // Əgər UserEntity-də bu sahə yoxdursa, bu hissəni kommentə al
                predicates.add(cb.equal(root.get("isDeleted"), false));
            }

            // 5. Sıralama
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}