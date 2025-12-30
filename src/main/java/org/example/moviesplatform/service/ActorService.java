package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.moviesplatform.dto.ActorDTO;
import org.example.moviesplatform.entity.Actor;
import org.example.moviesplatform.error.model.ActorNotFoundException;
import org.example.moviesplatform.mapper.ActorMapper;
import org.example.moviesplatform.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    public List<ActorDTO> getAllActors() {
        return actorMapper.toDTOList(actorRepository.findAll());
    }

    public ActorDTO getActorById(Integer id) {
        return actorRepository.findById(id)
                .map(actorMapper::toDTO)
                .orElseThrow(() -> new ActorNotFoundException("Actor not found"));
    }

    @Transactional
    public ActorDTO createActor(ActorDTO dto) {
        Actor actor = actorMapper.toEntity(dto);
        return actorMapper.toDTO(actorRepository.save(actor));
    }

    // ActorService.java daxilində
    @Transactional
    public void deleteActor(Integer id) {
        if (!actorRepository.existsById(id)) {
            throw new ActorNotFoundException("Actor not found with id: " + id);
        }
        actorRepository.deleteById(id);
    }
}