package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.moviesplatform.entity.Genre;
import org.example.moviesplatform.model.GenreFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Bu klass janrlar üzərində mürəkkəb və dinamik SQL sorğuları qurmaq üçündür.
 */
public class GenreSpecification {

    public static Specification<Genre> getSpecification(GenreFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Ad üzrə axtarış (Məsələn: 'act' yazanda 'Action' tapılır)
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }

            // 2. Yaradılma tarixi aralığı (Başlanğıc tarix)
            if (filter.getCreatedAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtFrom()));
            }

            // 3. Yaradılma tarixi aralığı (Son tarix)
            if (filter.getCreatedAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtTo()));
            }

            // Bütün şərtləri "AND" (VƏ) məntiqi ilə birləşdirir
            // Əgər heç bir filtr yoxdursa, SQL-də "1=1" kimi davranır və hamısını gətirir.
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}