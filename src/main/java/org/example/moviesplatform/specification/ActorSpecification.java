package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.moviesplatform.entity.Actor;
import org.example.moviesplatform.model.ActorFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Bu klass aktyorlar üzərində mürəkkəb axtarış sorğularını (SQL) dinamik şəkildə qurur.
 */
public class ActorSpecification {

    public static Specification<Actor> getSpecification(ActorFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getBiography() != null && !filter.getBiography().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("biography")),
                        "%" + filter.getBiography().toLowerCase() + "%"));
            }

            if (filter.getBirthDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("birthDate"), filter.getBirthDateFrom()));
            }
            if (filter.getBirthDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("birthDate"), filter.getBirthDateTo()));
            }

            if (filter.getDeathDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("deathDate"), filter.getDeathDateFrom()));
            }
            if (filter.getDeathDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("deathDate"), filter.getDeathDateTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}