package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.moviesplatform.entity.Director;
import org.example.moviesplatform.model.DirectorFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DirectorSpecification {

    public static Specification<Director> getSpecification(DirectorFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Ad üzrə filtr (case-insensitive)
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }

            // 2. Bioqrafiya üzrə filtr
            if (filter.getBiography() != null && !filter.getBiography().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("biography")),
                        "%" + filter.getBiography().toLowerCase() + "%"));
            }

            // 3. Doğum tarixi aralığı (birthDate)
            if (filter.getBirthDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("birthDate"), filter.getBirthDateFrom()));
            }
            if (filter.getBirthDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("birthDate"), filter.getBirthDateTo()));
            }

            // 4. Ölüm tarixi aralığı (deathDate) - YENİ ƏLAVƏ EDİLDİ
            if (filter.getDeathDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("deathDate"), filter.getDeathDateFrom()));
            }
            if (filter.getDeathDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("deathDate"), filter.getDeathDateTo()));
            }

            // Əgər filtr boşdursa, cb.and() boş array ilə 1=1 (bütün nəticələr) qaytarır
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}