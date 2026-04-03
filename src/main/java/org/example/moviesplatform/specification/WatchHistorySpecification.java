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

    public static Specification<WatchHistory> getSpecification(WatchHistoryFilter filter) {
        return new WatchHistorySpecification(filter);
    }

    @Override
    public Predicate toPredicate(Root<WatchHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter == null) return cb.and(new Predicate[0]);

        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get("userEntity").get("id"), filter.getUserId()));
        }

        if (filter.getMovieId() != null) {
            predicates.add(cb.equal(root.get("movie").get("id"), filter.getMovieId()));
        }

        if (filter.getIsCompleted() != null) {
            predicates.add(cb.equal(root.get("isCompleted"), filter.getIsCompleted()));
        }

        if (filter.getMinProgressPercentage() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("progressPercentage"), filter.getMinProgressPercentage()));
        }

        if (filter.getMaxProgressPercentage() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("progressPercentage"), filter.getMaxProgressPercentage()));
        }

        if (filter.getLastWatchedFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastWatchedAt"), filter.getLastWatchedFrom()));
        }

        if (filter.getLastWatchedTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("lastWatchedAt"), filter.getLastWatchedTo()));
        }

        if (filter.getMinWatchCount() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("watchCount"), filter.getMinWatchCount()));
        }

        if (query.getResultType() != Long.class && query.getResultType() != long.class) {
            query.orderBy(cb.desc(root.get("lastWatchedAt")));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}