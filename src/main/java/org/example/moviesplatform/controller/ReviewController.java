package org.example.moviesplatform.controller;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.ReviewDTO;
import org.example.moviesplatform.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // Müəyyən bir filmə yazılan bütün rəylər
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDTO>> getByMovie(@PathVariable Integer movieId) {
        return ResponseEntity.ok(reviewService.getReviewsByMovie(movieId));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@RequestBody ReviewDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
