package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.entity.Wishlist;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.mapper.WishlistMapper;
import org.example.moviesplatform.repository.MovieRepository;
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final WishlistMapper wishlistMapper;

    @Transactional(readOnly = true)
    public List<WishlistDTO> getWishlistByUserId(Integer userId) {
        log.info("Fetching wishlist for user {}", userId);

        if (!userRepository.existsById(Long.valueOf(userId))) {
            throw new UserNotFoundException("User not found: " + userId);
        }

        return wishlistMapper.toDTOList(wishlistRepository.findByUserEntity_IdOrderByCreatedAtDesc(userId));
    }

    @Transactional
    public WishlistDTO add(WishlistDTO dto) {
        if (!movieRepository.existsById(dto.getMovieId())) {
            throw new MovieNotFoundException("Movie not found: " + dto.getMovieId());
        }

        if (wishlistRepository.existsByIdUserIdAndIdMovieId(dto.getUserId(), dto.getMovieId())) {
            throw new RuntimeException("Movie already in wishlist!");
        }

        Wishlist wishlist = wishlistMapper.toEntity(dto);
        wishlist.setUserEntity(userRepository.getReferenceById(Long.valueOf(dto.getUserId())));
        wishlist.setMovie(movieRepository.getReferenceById(dto.getMovieId()));

        return wishlistMapper.toDTO(wishlistRepository.save(wishlist));
    }

    @Transactional
    public void remove(Integer userId, Integer movieId) {
        if (!wishlistRepository.existsByIdUserIdAndIdMovieId(userId, movieId)) {
            throw new RuntimeException("Wishlist item not found.");
        }
        wishlistRepository.deleteByUserIdAndMovieId(userId, movieId);
    }
}