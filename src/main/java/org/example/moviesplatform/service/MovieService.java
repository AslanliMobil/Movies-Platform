package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.MovieDTO;
import org.example.moviesplatform.entity.Actor;
import org.example.moviesplatform.entity.Director;
import org.example.moviesplatform.entity.Genre;
import org.example.moviesplatform.entity.Movie;
import org.example.moviesplatform.error.model.MovieNotFoundException;
import org.example.moviesplatform.mapper.MovieMapper;
import org.example.moviesplatform.model.MovieFilter;
import org.example.moviesplatform.repository.ActorRepository;
import org.example.moviesplatform.repository.DirectorRepository;
import org.example.moviesplatform.repository.GenreRepository;
import org.example.moviesplatform.repository.MovieRepository;
import org.example.moviesplatform.specification.MovieSpecification;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final MovieMapper movieMapper;
    private final HlsTranscoderService hlsTranscoderService;

    @Transactional(readOnly = true)
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        log.info("Fetching all active movies with pagination");
        return movieRepository.findAll(pageable).map(movieMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "movies", key = "#id", unless = "#result == null")
    public MovieDTO getMovieById(Integer id) {
        log.info("Fetching movie from DB for ID: {}", id);
        return movieRepository.findById(id)
                .map(movieMapper::toDTO)
                .orElseThrow(() -> new MovieNotFoundException("Aktiv film tapılmadı ID: " + id));
    }

    @Transactional
    public MovieDTO create(MovieDTO dto) {
        if (movieRepository.existsByTitleIgnoreCaseAndIsDeletedFalse(dto.getTitle())) {
            throw new RuntimeException("Bu adda aktiv film artıq mövcuddur: " + dto.getTitle());
        }
        Movie movie = movieMapper.toEntity(dto);
        syncRelations(movie, dto);
        Movie saved = movieRepository.save(movie);
        log.info("Yeni film yaradıldı: {}", saved.getTitle());
        return movieMapper.toDTO(saved);
    }

    @Transactional
    @CacheEvict(value = "movies", key = "#id")
    public MovieDTO update(Integer id, MovieDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Yeniləmək üçün film tapılmadı"));
        movieMapper.updateMovieFromDto(dto, movie);
        syncRelations(movie, dto);
        return movieMapper.toDTO(movieRepository.save(movie));
    }

    @Transactional
    @CacheEvict(value = "movies", key = "#id")
    public void delete(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Silinəcək film tapılmadı ID: " + id));
        movieRepository.delete(movie);
    }

    @Transactional
    @CacheEvict(value = "movies", key = "#id")
    public MovieDTO restore(Integer id) {
        Movie movie = movieRepository.findDeletedMovieById(id)
                .orElseThrow(() -> new MovieNotFoundException("Bərpa üçün silinmiş film tapılmadı: " + id));
        movie.setDeleted(false);
        return movieMapper.toDTO(movieRepository.save(movie));
    }

    @Transactional(readOnly = true)
    public Page<MovieDTO> search(MovieFilter filter, Pageable pageable) {
        return movieRepository.findAll(MovieSpecification.getSpecification(filter), pageable)
                .map(movieMapper::toDTO);
    }

    /**
     * Video emalı prosesini başladır.
     */
    public void processMovieVideo(Integer movieId, MultipartFile videoFile) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Film tapılmadı! ID: " + movieId));

        log.info("Video emalı prosesi tetiklendi. Movie ID: {}, Title: {}", movieId, movie.getTitle());

        Path tempFile = Files.createTempFile("raw_vid_" + movieId + "_", ".mp4");
        videoFile.transferTo(tempFile);

        log.info("Müvəqqəti fayl yaradıldı: {}", tempFile.toAbsolutePath());

        hlsTranscoderService.convertToHls(movieId.toString(), tempFile.toString());
    }

    /**
     * Transcoding bitdikdən sonra HlsTranscoderService tərəfindən çağırılır.
     * URL-in düzgünlüyünü (movies/ qovluğu) yoxlayır və bazanı yeniləyir.
     */
    @Transactional
    @CacheEvict(value = "movies", key = "#movieId")
    public void updateMovieVideoUrl(Integer movieId, String videoUrl) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("URL yeniləmək üçün film tapılmadı"));

        // DÜZƏLİŞ: MinIO strukturuna uyğunluq üçün URL manipulyasiyası
        String finalUrl = videoUrl;
        if (videoUrl != null && videoUrl.contains("movie-videos/")) {
            // Əgər URL-də "movie-videos/" var, amma "movies/" qovluğu yoxdursa, əlavə edirik
            if (!videoUrl.contains("movie-videos/movies/")) {
                finalUrl = videoUrl.replace("movie-videos/", "movie-videos/movies/");
                log.warn("Video URL-i MinIO strukturuna (movies/ qovluğuna) uyğunlaşdırıldı: {}", finalUrl);
            }
        }

        movie.setVideoUrl(finalUrl);
        movieRepository.save(movie);
        log.info("Bazada Video URL yeniləndi: Movie ID {}, Final URL: {}", movieId, finalUrl);
    }

    private void syncRelations(Movie movie, MovieDTO dto) {
        if (dto.getDirector() != null && dto.getDirector().getId() != null) {
            Director director = directorRepository.findById(dto.getDirector().getId())
                    .orElseThrow(() -> new RuntimeException("Rejissor tapılmadı"));
            movie.setDirector(director);
        }
        if (dto.getGenres() != null) {
            List<Genre> genres = dto.getGenres().stream()
                    .map(g -> genreRepository.findById(g.getId()).orElseThrow())
                    .collect(Collectors.toList());
            movie.setGenres(genres);
        }
        if (dto.getActors() != null) {
            List<Actor> actors = dto.getActors().stream()
                    .map(a -> actorRepository.findById(a.getId()).orElseThrow())
                    .collect(Collectors.toList());
            movie.setActors(actors);
        }
    }
}