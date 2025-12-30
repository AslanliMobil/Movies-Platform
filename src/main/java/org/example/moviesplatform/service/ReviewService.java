package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.ReviewDTO;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.entity.Review;
import org.example.moviesplatform.entity.User;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.error.model.ReviewNotFoundException;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.mapper.ReviewMapper;
import org.example.moviesplatform.repository.MovieRepository;
import org.example.moviesplatform.repository.ReviewRepository;
import org.example.moviesplatform.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Müəyyən bir filmə yazılmış bütün rəyləri gətirir.
     */
    public List<ReviewDTO> getReviewsByMovie(Integer movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieNotFoundException("Movie not found with id: " + movieId);
        }
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return reviewMapper.toDTOList(reviews);
    }

    /**
     * Yeni rəy əlavə edir.
     */
    @Transactional
    public ReviewDTO addReview(ReviewDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + dto.getUserId()));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + dto.getMovieId()));

        Review review = reviewMapper.toEntity(dto);
        review.setUser(user);
        review.setMovie(movie);
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);
        log.info("User '{}' added a review for movie '{}'", user.getUsername(), movie.getTitle());

        return reviewMapper.toDTO(saved);
    }

    /**
     * Rəyi silir.
     */
    @Transactional
    public void delete(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
        log.info("Review with id {} was deleted", id);
    }
}