package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.DirectorDTO;
import org.example.moviesplatform.entity.Director;
import org.example.moviesplatform.error.model.DirectorNotFoundException;
import org.example.moviesplatform.mapper.DirectorMapper;
import org.example.moviesplatform.repository.DirectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public List<DirectorDTO> getAllDirectors() {
        return directorMapper.toDTOList(directorRepository.findAll());
    }

    public DirectorDTO getDirectorById(Integer id) {
        return directorRepository.findById(id)
                .map(directorMapper::toDTO)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found"));
    }

    @Transactional
    public DirectorDTO createDirector(DirectorDTO dto) {
        Director director = directorMapper.toEntity(dto);
        Director saved = directorRepository.save(director);
        log.info("Director added: {}", saved.getName());
        return directorMapper.toDTO(saved);
    }

    @Transactional
    public void deleteDirector(Integer id) {
        if (!directorRepository.existsById(id)) {
            throw new DirectorNotFoundException("Director not found");
        }
        directorRepository.deleteById(id);
    }
}
