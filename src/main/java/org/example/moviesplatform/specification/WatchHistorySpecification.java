package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.moviesplatform.entity.WatchHistory;
import org.example.moviesplatform.model.WatchHistoryFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WatchHistorySpecification implements Specification<WatchHistory> {
    private final WatchHistoryFilter filter;

    public WatchHistorySpecification(WatchHistoryFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<WatchHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), filter.getUserId()));
        }

        if (filter.getIsFinished() != null) {
            predicates.add(cb.equal(root.get("isFinished"), filter.getIsFinished()));
        }

        if (filter.getLastWatchedFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastWatchedAt"), filter.getLastWatchedFrom()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}