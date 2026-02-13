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

            // 1. Ad üzrə axtarış (Tom -> tom, TOM fərqi qoymadan)
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }

            // 2. Bioqrafiya daxilində söz axtarışı
            if (filter.getBiography() != null && !filter.getBiography().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("biography")),
                        "%" + filter.getBiography().toLowerCase() + "%"));
            }

            // 3. Doğum tarixi aralığı (Məsələn: 1980-ci ildən sonra doğulanlar)
            if (filter.getBirthDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("birthDate"), filter.getBirthDateFrom()));
            }
            if (filter.getBirthDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("birthDate"), filter.getBirthDateTo()));
            }

            // 4. Ölüm tarixi aralığı
            if (filter.getDeathDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("deathDate"), filter.getDeathDateFrom()));
            }
            if (filter.getDeathDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("deathDate"), filter.getDeathDateTo()));
            }

            // Bütün şərtləri "AND" (VƏ) məntiqi ilə birləşdirir
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}