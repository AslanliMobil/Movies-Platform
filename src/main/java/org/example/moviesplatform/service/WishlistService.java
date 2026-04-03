package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.entity.Wishlist;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.error.model.ResourceAlreadyExistsException;
import org.example.moviesplatform.error.model.UserNotFoundException;
import org.example.moviesplatform.error.model.WishlistNotFoundException;
import org.example.moviesplatform.mapper.WishlistMapper;
import org.example.moviesplatform.model.WishlistFilter;
import org.example.moviesplatform.repository.MovieRepository;
import org.example.moviesplatform.repository.WishlistRepository;
import org.example.moviesplatform.security.repository.UserRepository;
import org.example.moviesplatform.specification.WishlistSpecification;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public List<WishlistDTO> search(WishlistFilter filter) {
        log.info("İstək siyahısında filtrlə axtarış: {}", filter);
        List<Wishlist> wishlists = wishlistRepository.findAll(WishlistSpecification.getSpecification(filter));
        return wishlistMapper.toDTOList(wishlists);
    }

    @Transactional(readOnly = true)
    public List<WishlistDTO> getWishlistByUserId(Integer userId) {
        log.info("İstifadəçi üçün istək siyahısı gətirilir: {}", userId);

        if (!userRepository.existsById(userId.longValue())) {
            log.error("Məlumat bazasında ID-si {} olan istifadəçi tapılmadı", userId);
            throw new UserNotFoundException("İstifadəçi tapılmadı: " + userId);
        }

        List<Wishlist> entities = wishlistRepository.findByUserEntity_IdOrderByCreatedAtDesc(userId);
        return wishlistMapper.toDTOList(entities);
    }

    @Transactional
    public WishlistDTO add(WishlistDTO dto) {
        if (!movieRepository.existsById(dto.getMovieId())) {
            throw new MovieNotFoundException("Film tapılmadı: " + dto.getMovieId());
        }

        if (wishlistRepository.existsByIdUserIdAndIdMovieId(dto.getUserId(), dto.getMovieId())) {
            throw new ResourceAlreadyExistsException("Bu film artıq istək siyahınızdadır!");
        }

        Wishlist wishlist = wishlistMapper.toEntity(dto);
        wishlist.setUserEntity(userRepository.getReferenceById(dto.getUserId().longValue()));
        wishlist.setMovie(movieRepository.getReferenceById(dto.getMovieId()));

        return wishlistMapper.toDTO(wishlistRepository.save(wishlist));
    }

    @Transactional
    public void remove(Integer userId, Integer movieId) {
        Wishlist wishlist = wishlistRepository.findByIdUserIdAndIdMovieId(userId, movieId)
                .orElseThrow(() -> new WishlistNotFoundException("Bu film istək siyahısında tapılmadı."));

        String currentLoggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !wishlist.getUserEntity().getUsername().equals(currentLoggedUsername)) {
            log.warn("Təhlükəsizlik xəbərdarlığı: İstifadəçi {} başqasının siyahısına müdaxilə etməyə çalışdı!", currentLoggedUsername);
            throw new RuntimeException("Təhlükəsizlik xətası: Siz başqasının istək siyahısından film silə bilməzsiniz!");
        }

        wishlistRepository.delete(wishlist);
        log.info("Film istək siyahısından silindi. User: {}, Movie: {}", userId, movieId);
    }
}