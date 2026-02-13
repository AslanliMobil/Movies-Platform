package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.WishlistDTO;
import org.example.moviesplatform.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WishlistMapper {

    /**
     * Entity -> DTO
     */
    @Mapping(source = "id.userId", target = "userId")
    @Mapping(source = "id.movieId", target = "movieId")
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "movie.coverImageUrl", target = "movieCoverUrl")
    WishlistDTO toDTO(Wishlist wishlist);

    /**
     * DTO -> Entity
     */
    @Mapping(source = "userId", target = "id.userId")
    @Mapping(source = "movieId", target = "id.movieId")
    // DÜZƏLİŞ: "user.id" yerinə "userEntity.id" yazırıq
    @Mapping(source = "userId", target = "userEntity.id")
    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(target = "createdAt", ignore = true)
    Wishlist toEntity(WishlistDTO dto);

    List<WishlistDTO> toDTOList(List<Wishlist> wishlists);
}