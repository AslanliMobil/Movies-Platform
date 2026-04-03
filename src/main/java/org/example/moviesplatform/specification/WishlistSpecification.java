package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.entity.Wishlist;
import org.example.moviesplatform.model.WishlistFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WishlistSpecification implements Specification<Wishlist> {

    private final WishlistFilter filter;

    @Override
    public Predicate toPredicate(Root<Wishlist> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get("id").get("userId"), filter.getUserId()));
        }

        if (filter.getMovieId() != null) {
            predicates.add(cb.equal(root.get("id").get("movieId"), filter.getMovieId()));
        }

        if (filter.getMovieTitle() != null && !filter.getMovieTitle().isBlank()) {
            Join<Wishlist, Movie> movieJoin = root.join("movie");
            predicates.add(cb.like(cb.lower(movieJoin.get("title")),
                    "%" + filter.getMovieTitle().toLowerCase() + "%"));
        }

        if (filter.getGenreId() != null) {
            Join<Wishlist, Movie> movieJoin = root.join("movie");
            predicates.add(cb.equal(movieJoin.join("genres").get("id"), filter.getGenreId()));
        }

        if (filter.getAddedFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getAddedFrom()));
        }
        if (filter.getAddedTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getAddedTo()));
        }

        if (filter.getHasNote() != null) {
            if (Boolean.TRUE.equals(filter.getHasNote())) {
                predicates.add(cb.isNotNull(root.get("note")));
            } else {
                predicates.add(cb.isNull(root.get("note")));
            }
        }

        query.distinct(true);

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    public static Specification<Wishlist> getSpecification(WishlistFilter filter) {
        return new WishlistSpecification(filter);
    }
}