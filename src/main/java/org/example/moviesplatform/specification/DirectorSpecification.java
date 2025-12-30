package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.Director;
import org.example.moviesplatform.model.DirectorFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DirectorSpecification implements Specification<Director> {
    private final DirectorFilter filter;

    public DirectorSpecification(DirectorFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Director> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Ad üzrə filtr (like %name%)
        if (filter.getName() != null && !filter.getName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + filter.getName().toLowerCase().trim() + "%"));
        }

        // Doğum ili aralığı - Başlanğıc (From)
        if (filter.getBirthYearFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("birthYear"), filter.getBirthYearFrom()));
        }

        // Doğum ili aralığı - Son (To)
        if (filter.getBirthYearTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("birthYear"), filter.getBirthYearTo()));
        }

        // Əgər heç bir filtr göndərilməyibsə, cb.conjunction() bütün nəticələri qaytarır (1=1)
        return predicates.isEmpty()
                ? cb.conjunction()
                : cb.and(predicates.toArray(new Predicate[0]));
    }
}