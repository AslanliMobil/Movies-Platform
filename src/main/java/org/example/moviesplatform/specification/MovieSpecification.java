package org.example.moviesplatform.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.moviesplatform.entity.Actor;
import org.example.moviesplatform.entity.Genre;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.model.MovieFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecification {

    public static Specification<Movie> getSpecification(MovieFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 0. Həmişə yalnız silinməmiş filmləri gətir (Soft Delete Protection)
            // Qeyd: @Where annotasiyası olsa belə, bura yazmaq daha sığortalıdır.
            predicates.add(cb.equal(root.get("isDeleted"), false));

            // 1. Başlığa görə axtarış
            if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")),
                        "%" + filter.getTitle().toLowerCase() + "%"));
            }

            // 2. Buraxılış tarixi aralığı
            if (filter.getReleaseDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("releaseDate"), filter.getReleaseDateFrom()));
            }
            if (filter.getReleaseDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("releaseDate"), filter.getReleaseDateTo()));
            }

            // 3. Reytinq aralığı
            if (filter.getRatingFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("averageRating"), filter.getRatingFrom()));
            }
            if (filter.getRatingTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("averageRating"), filter.getRatingTo()));
            }

            // 4. Müddət (Duration) aralığı
            if (filter.getDurationFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("duration"), filter.getDurationFrom()));
            }
            if (filter.getDurationTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("duration"), filter.getDurationTo()));
            }

            // 5. Rejissora görə süzmə
            if (filter.getDirectorId() != null) {
                predicates.add(cb.equal(root.get("director").get("id"), filter.getDirectorId()));
            }

            // 6. Janra görə süzmə
            if (filter.getGenreId() != null) {
                Join<Movie, Genre> genreJoin = root.join("genres");
                predicates.add(cb.equal(genreJoin.get("id"), filter.getGenreId()));
                query.distinct(true);
            }

            // 7. Aktyora görə süzmə
            if (filter.getActorId() != null) {
                Join<Movie, Actor> actorJoin = root.join("actors");
                predicates.add(cb.equal(actorJoin.get("id"), filter.getActorId()));
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}