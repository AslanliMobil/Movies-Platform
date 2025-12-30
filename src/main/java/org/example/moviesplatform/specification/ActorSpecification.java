package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.Actor;
import org.example.moviesplatform.model.ActorFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ActorSpecification implements Specification<Actor> {
    private final ActorFilter filter;

    public ActorSpecification(ActorFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Actor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getBirthYearFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("birthYear"), filter.getBirthYearFrom()));
        }

        if (filter.getBirthYearTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("birthYear"), filter.getBirthYearTo()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}