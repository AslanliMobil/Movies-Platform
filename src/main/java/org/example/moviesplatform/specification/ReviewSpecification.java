package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.moviesplatform.entity.Review;
import org.example.moviesplatform.model.ReviewFilter;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification {

    public static Specification<Review> getSpecification(ReviewFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) return cb.conjunction();

            // 1. Filmə görə filtr (Join obyektindən istifadə edərək)
            if (filter.getMovieId() != null) {
                // Join istifadə etmək root.get("movie").get("id")-dən daha sağlamdır
                predicates.add(cb.equal(root.join("movie").get("id"), filter.getMovieId()));
            }

            // 2. İstifadəçiyə görə filtr
            if (filter.getUserId() != null) {
                predicates.add(cb.equal(root.join("user").get("id"), filter.getUserId()));
            }

            // 3. Reytinq aralığı
            if (filter.getRatingFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating").as(Double.class), filter.getRatingFrom()));
            }
            if (filter.getRatingTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("rating").as(Double.class), filter.getRatingTo()));
            }

            // 4. Tarix aralığı
            if (filter.getCreatedAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt").as(LocalDateTime.class), filter.getCreatedAtFrom()));
            }
            if (filter.getCreatedAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt").as(LocalDateTime.class), filter.getCreatedAtTo()));
            }

            // ORDER BY hissəsi (Yalnız əsas sorğu üçün)
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(cb.desc(root.get("createdAt")));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}