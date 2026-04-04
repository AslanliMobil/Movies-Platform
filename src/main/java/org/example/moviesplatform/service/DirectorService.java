package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.DirectorDTO;
import org.example.moviesplatform.entity.Director;
import org.example.moviesplatform.error.model.DirectorNotFoundException;
import org.example.moviesplatform.error.model.ResourceAlreadyExistsException;
import org.example.moviesplatform.mapper.DirectorMapper;
import org.example.moviesplatform.model.DirectorFilter;
import org.example.moviesplatform.repository.DirectorRepository;
import org.example.moviesplatform.specification.DirectorSpecification;
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
        log.info("Fetching all directors");
        return directorMapper.toDTOList(directorRepository.findAll());
    }


    public DirectorDTO getDirectorById(Integer id) {
        log.info("Fetching director with id: {}", id);
        return directorRepository.findById(id)
                .map(directorMapper::toDTO)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found with id: " + id));
    }


    @Transactional
    public DirectorDTO createDirector(DirectorDTO dto) {
        if (directorRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new ResourceAlreadyExistsException("Bu adda rejissor artıq sistemdə mövcuddur: " + dto.getName());
        }

        if (dto.getDeathDate() != null && dto.getBirthDate() != null) {
            if (dto.getDeathDate().isBefore(dto.getBirthDate())) {
                throw new IllegalArgumentException("Ölüm tarixi doğum tarixindən əvvəl ola bilməz!");
            }
        }

        Director director = directorMapper.toEntity(dto);
        Director saved = directorRepository.save(director);
        return directorMapper.toDTO(saved);
    }


    @Transactional
    public DirectorDTO updateDirector(Integer id, DirectorDTO dto) {
        log.info("Updating director with id: {}", id);
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found with id: " + id));

        if (!director.getName().equalsIgnoreCase(dto.getName()) &&
                directorRepository.existsByNameIgnoreCase(dto.getName().trim())) {
            throw new ResourceAlreadyExistsException("Bu adda rejissor artıq mövcuddur!");
        }

        if (dto.getDeathDate() != null && dto.getBirthDate() != null) {
            if (dto.getDeathDate().isBefore(dto.getBirthDate())) {
                throw new IllegalArgumentException("Ölüm tarixi doğum tarixindən əvvəl ola bilməz!");
            }
        }

        director.setName(dto.getName().trim());
        director.setBirthDate(dto.getBirthDate());
        director.setBiography(dto.getBiography());
        director.setDeathDate(dto.getDeathDate());

        Director updated = directorRepository.save(director);
        return directorMapper.toDTO(updated);
    }

    @Transactional
    public DirectorDTO patchDirector(Integer id, DirectorDTO dto) {
        log.info("Patching director with id: {}", id);
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new DirectorNotFoundException("Director not found with id: " + id));

        if (dto.getName() != null) {
            if (!director.getName().equalsIgnoreCase(dto.getName()) &&
                    directorRepository.existsByNameIgnoreCase(dto.getName().trim())) {
                throw new ResourceAlreadyExistsException("Bu adda rejissor artıq mövcuddur!");
            }
            director.setName(dto.getName().trim());
        }

        if (dto.getBiography() != null) {
            director.setBiography(dto.getBiography());
        }

        if (dto.getBirthDate() != null) {
            director.setBirthDate(dto.getBirthDate());
        }

        if (dto.getDeathDate() != null) {
            director.setDeathDate(dto.getDeathDate());
        }

        if (director.getDeathDate() != null && director.getBirthDate() != null) {
            if (director.getDeathDate().isBefore(director.getBirthDate())) {
                throw new IllegalArgumentException("Ölüm tarixi doğum tarixindən əvvəl ola bilməz!");
            }
        }

        return directorMapper.toDTO(directorRepository.save(director));
    }


    @Transactional
    public void deleteDirector(Integer id) {
        log.info("Deleting director with id: {}", id);
        if (!directorRepository.existsById(id)) {
            throw new DirectorNotFoundException("Director not found with id: " + id);
        }
        directorRepository.deleteById(id);
    }


    public List<DirectorDTO> search(DirectorFilter filter) {
        log.info("Searching directors with criteria: {}", filter);
        List<Director> directors = directorRepository.findAll(DirectorSpecification.getSpecification(filter));
        return directorMapper.toDTOList(directors);
    }
}