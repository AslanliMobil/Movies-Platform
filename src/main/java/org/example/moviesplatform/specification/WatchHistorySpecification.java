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

        // 1. İstifadəçi üzrə filtr
        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), filter.getUserId()));
        }

        // 2. Konkret Film üzrə filtr
        if (filter.getMovieId() != null) {
            predicates.add(cb.equal(root.get("movie").get("id"), filter.getMovieId()));
        }

        // 3. Tamamlanma statusu (Netflix: isCompleted)
        if (filter.getIsCompleted() != null) {
            predicates.add(cb.equal(root.get("isCompleted"), filter.getIsCompleted()));
        }

        // 4. Proqres faizi: Minimum (məs: 50%-dən çox baxılanlar)
        if (filter.getMinProgressPercentage() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("progressPercentage"), filter.getMinProgressPercentage()));
        }

        // 5. Proqres faizi: Maksimum (məs: Hələ 20%-nə qədər baxılanlar)
        if (filter.getMaxProgressPercentage() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("progressPercentage"), filter.getMaxProgressPercentage()));
        }

        // 6. Tarix aralığı: Başlanğıc
        if (filter.getLastWatchedFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastWatchedAt"), filter.getLastWatchedFrom()));
        }

        // 7. Tarix aralığı: Son
        if (filter.getLastWatchedTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("lastWatchedAt"), filter.getLastWatchedTo()));
        }

        // 8. Analitika: Minimum baxış sayı (Məs: Ən az 3 dəfə baxılanlar)
        if (filter.getMinWatchCount() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("watchCount"), filter.getMinWatchCount()));
        }

        // Nəticələri həmişə son baxılma tarixinə görə sıralayırıq
        query.orderBy(cb.desc(root.get("lastWatchedAt")));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}