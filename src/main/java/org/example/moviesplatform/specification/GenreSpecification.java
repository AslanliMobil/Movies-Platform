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

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filter.getName().toLowerCase() + "%"));
            }

            if (filter.getCreatedAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedAtTo()));
            }


            if (filter.getMinMovieCount() != null && filter.getMinMovieCount() > 0) {
                predicates.add(cb.greaterThanOrEqualTo(cb.size(root.get("movies")), filter.getMinMovieCount()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}