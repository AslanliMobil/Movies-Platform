package org.example.moviesplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.ReviewDTO;
import org.example.moviesplatform.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping("/movie/{movieId}")
    public ResponseEntity<Page<ReviewDTO>> getByMovie(
            @PathVariable Integer movieId,
            Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewsByMovie(movieId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReviewDTO>> search(org.example.moviesplatform.model.ReviewFilter filter, Pageable pageable) {
        return ResponseEntity.ok(reviewService.searchReviews(filter, pageable));
    }


    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@Valid @RequestBody ReviewDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(dto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewDTO dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}