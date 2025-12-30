package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.Genre;
import org.example.moviesplatform.model.GenreFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenreSpecification implements Specification<Genre> {
    private final GenreFilter filter;

    public GenreSpecification(GenreFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Genre> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + filter.getName().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
