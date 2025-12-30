package org.example.moviesplatform.specification;

import org.example.moviesplatform.model.UserFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.example.moviesplatform.entity.User;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private final UserFilter filter;

    public UserSpecification(UserFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Username filter - null/boş dəyərlər nəzərə alınmır
        if (filter.getUsername() != null && !filter.getUsername().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("username")),
                    "%" + filter.getUsername().toLowerCase().trim() + "%"));
        }

        // Email filter - null/boş dəyərlər nəzərə alınmır
        if (filter.getEmail() != null && !filter.getEmail().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("email")),
                    "%" + filter.getEmail().toLowerCase().trim() + "%"));
        }

        // First name filter - null/boş dəyərlər nəzərə alınmır
        if (filter.getFirstName() != null && !filter.getFirstName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("firstName")),
                    "%" + filter.getFirstName().toLowerCase().trim() + "%"));
        }

        // Last name filter - null/boş dəyərlər nəzərə alınmır
        if (filter.getLastName() != null && !filter.getLastName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("lastName")),
                    "%" + filter.getLastName().toLowerCase().trim() + "%"));
        }

        if (filter.getCreatedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtFrom().toLocalDate()));
        }

        if (filter.getCreatedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtTo().toLocalDate()));
        }

        // Əgər heç bir filtr yoxdursa, bütün userləri qaytar
        return predicates.isEmpty()
                ? cb.conjunction()
                : cb.and(predicates.toArray(new Predicate[0]));
    }
}