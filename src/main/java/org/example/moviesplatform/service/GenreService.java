package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.GenreDTO;
import org.example.moviesplatform.entity.Genre;
import org.example.moviesplatform.error.model.GenreNotFoundException;
import org.example.moviesplatform.mapper.GenreMapper;
import org.example.moviesplatform.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public List<GenreDTO> getAll() {
        return genreMapper.toDTOList(genreRepository.findAll());
    }

    public GenreDTO getById(Integer id) {
        return genreRepository.findById(id)
                .map(genreMapper::toDTO)
                .orElseThrow(() -> new GenreNotFoundException("Genre not found with id: " + id));
    }

    @Transactional
    public GenreDTO create(GenreDTO dto) {
        Genre genre = genreMapper.toEntity(dto);
        Genre saved = genreRepository.save(genre);
        log.info("New genre created: {}", saved.getName());
        return genreMapper.toDTO(saved);
    }
}
