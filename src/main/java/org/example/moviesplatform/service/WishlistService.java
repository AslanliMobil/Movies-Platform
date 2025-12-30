package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.entity.Wishlist;
import org.example.moviesplatform.entity.WishlistId;
import org.example.moviesplatform.mapper.WishlistMapper;
import org.example.moviesplatform.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;

    public List<WishlistDTO> getWishlistByUserId(Integer userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        return wishlistMapper.toDTOList(wishlists);
    }

    @Transactional
    public WishlistDTO add(WishlistDTO dto) {
        Wishlist wishlist = wishlistMapper.toEntity(dto);

        // Composite Key-in set olunması
        WishlistId id = new WishlistId();
        id.setUserId(dto.getUserId());
        id.setMovieId(dto.getMovieId());
        wishlist.setId(id);

        wishlist.setCreatedAt(LocalDateTime.now());
        Wishlist saved = wishlistRepository.save(wishlist);
        return wishlistMapper.toDTO(saved);
    }

    @Transactional
    public void remove(Integer userId, Integer movieId) {
        wishlistRepository.deleteByUserIdAndMovieId(userId, movieId);
    }
}
