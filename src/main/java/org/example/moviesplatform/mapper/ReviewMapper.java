package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.ReviewDTO;
import org.example.moviesplatform.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "movie.id", target = "movieId")
    ReviewDTO toDTO(Review review);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "movieId", target = "movie.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toEntity(ReviewDTO dto);

    List<ReviewDTO> toDTOList(List<Review> reviews);
}