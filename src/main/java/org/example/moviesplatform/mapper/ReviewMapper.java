package org.example.moviesplatform.mapper;

import org.example.moviesplatform.dto.ReviewDTO;
import org.example.moviesplatform.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "movie.id", target = "movieId")
    ReviewDTO toDTO(Review review);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "movieId", target = "movie.id")
    Review toEntity(ReviewDTO dto);

    // Xətanı aradan qaldıran metod:
    List<ReviewDTO> toDTOList(List<Review> reviews);
}