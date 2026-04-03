package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.Role;
import org.example.moviesplatform.model.RoleFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RoleSpecification implements Specification<Role> {

    private final RoleFilter filter;

    public RoleSpecification(RoleFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter == null) return cb.conjunction();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("name").as(String.class)),
                    "%" + filter.getName().toLowerCase() + "%"
            ));
        }

        if (filter.getDescription() != null && !filter.getDescription().isBlank()) {
            predicates.add(cb.like(
                    cb.lower(root.get("description").as(String.class)),
                    "%" + filter.getDescription().toLowerCase() + "%"
            ));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}