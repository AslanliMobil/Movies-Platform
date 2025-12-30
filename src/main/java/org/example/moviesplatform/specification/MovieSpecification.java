package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.model.MovieFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecification implements Specification<Movie> {
    private final MovieFilter filter;

    public MovieSpecification(MovieFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));
        }

        if (filter.getReleaseYear() != null) {
            predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("releaseDate")), filter.getReleaseYear()));
        }

        if (filter.getRatingFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("averageRating"), filter.getRatingFrom()));
        }

        // Genre_id ilə süzmək (Join əməliyyatı)
        if (filter.getGenreId() != null) {
            predicates.add(cb.equal(root.join("genres").get("id"), filter.getGenreId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
