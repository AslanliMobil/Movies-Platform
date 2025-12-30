package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.Wishlist;
import org.example.moviesplatform.model.WishlistFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WishlistSpecification implements Specification<Wishlist> {
    private final WishlistFilter filter;

    public WishlistSpecification(WishlistFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Wishlist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // EmbeddedId daxilindəki sahələrə giriş
        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get("id").get("userId"), filter.getUserId()));
        }

        if (filter.getMovieId() != null) {
            predicates.add(cb.equal(root.get("id").get("movie").get("id"), filter.getMovieId()));
        }

        if (filter.getAddedFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getAddedFrom()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
