package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.Review;
import org.example.moviesplatform.model.ReviewFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReviewSpecification implements Specification<Review> {
    private final ReviewFilter filter;

    public ReviewSpecification(ReviewFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Review> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getMovieId() != null) {
            predicates.add(cb.equal(root.get("movie").get("id"), filter.getMovieId()));
        }

        if (filter.getRatingFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), filter.getRatingFrom()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
