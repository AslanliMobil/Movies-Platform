package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.ReviewDTO;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.entity.Review;
import org.example.moviesplatform.security.repository.entity.UserEntity;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.error.model.ResourceAlreadyExistsException;
import org.example.moviesplatform.error.model.ReviewNotFoundException;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.mapper.ReviewMapper;
import org.example.moviesplatform.repository.ReviewRepository;
import org.example.moviesplatform.repository.MovieRepository;
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.model.ReviewFilter;
import org.example.moviesplatform.specification.ReviewSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional(readOnly = true)
    public Page<ReviewDTO> searchReviews(ReviewFilter filter, Pageable pageable) {
        return reviewRepository.findAll(ReviewSpecification.getSpecification(filter), pageable)
                .map(reviewMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDTO> getReviewsByMovie(Integer movieId, Pageable pageable) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException("Film tapılmadı: " + movieId);
        }
        return reviewRepository.findByMovieId(movieId, pageable)
                .map(reviewMapper::toDTO);
    }

    @Transactional
    public ReviewDTO addReview(ReviewDTO dto) {
        if (reviewRepository.existsByUserIdAndMovieId(dto.getUserId().longValue(), dto.getMovieId())) {
            throw new ResourceAlreadyExistsException("Siz artıq bu filmə rəy yazmısınız.");
        }

        UserEntity user = userRepository.findById(dto.getUserId().longValue())
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı: " + dto.getUserId()));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException("Film tapılmadı: " + dto.getMovieId()));

        Review review = reviewMapper.toEntity(dto);
        review.setUser(user);
        review.setMovie(movie);

        Review saved = reviewRepository.saveAndFlush(review);
        updateMovieAverageRating(movie.getId());

        log.info("İstifadəçi '{}' '{}' filminə {} xal verdi.", user.getUsername(), movie.getTitle(), dto.getRating());
        return reviewMapper.toDTO(saved);
    }

    @Transactional
    public ReviewDTO updateReview(Integer id, ReviewDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Rəy tapılmadı: " + id));

        if (review.getUser().getId().intValue() != dto.getUserId().intValue()) {
            throw new RuntimeException("Siz başqasının rəyini dəyişə bilməzsiniz!");
        }

        review.setComment(dto.getComment());
        review.setRating(dto.getRating());

        Review updated = reviewRepository.saveAndFlush(review);
        updateMovieAverageRating(review.getMovie().getId());

        return reviewMapper.toDTO(updated);
    }

    @Transactional
    public void delete(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Rəy tapılmadı: " + id));

        Integer movieId = review.getMovie().getId();
        reviewRepository.delete(review);

        reviewRepository.flush();

        updateMovieAverageRating(movieId);

        log.info("ID {} olan rəy silindi.", id);
    }

    private void updateMovieAverageRating(Integer movieId) {
        Double average = reviewRepository.calculateAverageRating(movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Film tapılmadı"));

        movie.setAverageRating(average != null ? average : 0.0);
        movieRepository.save(movie);
    }
}