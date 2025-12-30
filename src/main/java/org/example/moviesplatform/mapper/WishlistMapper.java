package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "movie.coverImageUrl", target = "movieCoverUrl")
    WishlistDTO toDTO(Wishlist wishlist);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "movieId", target = "movie.id")
    Wishlist toEntity(WishlistDTO dto);

    List<WishlistDTO> toDTOList(List<Wishlist> wishlists);
}
