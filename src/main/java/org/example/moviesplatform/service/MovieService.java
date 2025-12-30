package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.MovieDTO;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.mapper.MovieMapper;
import org.example.moviesplatform.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable).map(movieMapper::toDTO);
    }

    public MovieDTO getMovieById(Integer id) {
        return movieRepository.findById(id)
                .map(movieMapper::toDTO)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + id));
    }

    @Transactional
    public MovieDTO create(MovieDTO dto) {
        Movie movie = movieMapper.toEntity(dto);
        movie.setCreatedAt(LocalDateTime.now());
        Movie saved = movieRepository.save(movie);
        log.info("Created new movie: {}", saved.getTitle());
        return movieMapper.toDTO(saved);
    }

    @Transactional
    public void delete(Integer id) {
        if (!movieRepository.existsById(id)) {
            throw new MovieNotFoundException("Movie not found");
        }
        movieRepository.deleteById(id);
    }
}
