package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.ActorDTO;
import org.example.moviesplatform.entity.Actor;
import org.example.moviesplatform.error.model.ActorNotFoundException;
import org.example.moviesplatform.mapper.ActorMapper;
import org.example.moviesplatform.model.ActorFilter;
import org.example.moviesplatform.repository.ActorRepository;
import org.example.moviesplatform.specification.ActorSpecification; // Specification klassı tələb olunur
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;

    /**
     * 1. BÜTÜN AKTYORLARI GƏTİRMƏK
     * Məqsəd: Bazadakı hər kəsi siyahı şəklində görmək.
     * İş prinsipi: Bazadakı bütün 'Actor' məlumatlarını götürür və onları
     * istifadəçiyə uyğun 'ActorDTO' formatına çevirir.
     */
    public List<ActorDTO> getAllActors() {
        log.info("Fetching all actors");
        return actorMapper.toDTOList(actorRepository.findAll());
    }

    /**
     * 2. ID-YƏ GÖRƏ AKTYOR TAPMAQ
     * Məqsəd: Yalnız bir nəfərin detallarını öyrənmək.
     * İş prinsipi: Verilən ID-ni bazada axtarır. Əgər yoxdursa, 'ActorNotFoundException'
     * xətası fırladaraq proqramın səhv məlumatla işləməsinin qarşısını alır.
     */
    public ActorDTO getActorById(Integer id) {
        log.info("Fetching actor with id: {}", id);
        return actorRepository.findById(id)
                .map(actorMapper::toDTO)
                .orElseThrow(() -> new ActorNotFoundException("Actor not found with id: " + id));
    }

    /**
     * 3. YENİ AKTYOR YARATMAQ
     * Məqsəd: Bazaya yeni bir aktyor qeydi əlavə etmək.
     * Biznes Qaydaları:
     * - Eyni adda aktyor təkrar qeydiyyatdan keçə bilməz.
     * - Ölüm tarixi doğum tarixindən daha köhnə ola bilməz.
     */
    @Transactional
    public ActorDTO createActor(ActorDTO dto) {
        log.info("Creating new actor: {}", dto.getName());

        if (actorRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Bu adda aktyor artıq mövcuddur: " + dto.getName());
        }

        validateDates(dto.getBirthDate(), dto.getDeathDate());

        Actor actor = actorMapper.toEntity(dto);
        return actorMapper.toDTO(actorRepository.save(actor));
    }

    /**
     * 4. TAM YENİLƏMƏ (PUT)
     * Məqsəd: Aktyorun bütün məlumatlarını (ad, bioqrafiya və s.) yenidən yazmaq.
     * İş prinsipi: Mövcud aktyoru tapır və onun bütün köhnə məlumatlarını
     * göndərilən yeni məlumatlarla tamamilə əvəzləyir.
     */
    @Transactional
    public ActorDTO updateActor(Integer id, ActorDTO dto) {
        log.info("Updating actor with id: {}", id);
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ActorNotFoundException("Actor not found with id: " + id));

        validateDates(dto.getBirthDate(), dto.getDeathDate());

        actor.setName(dto.getName());
        actor.setBirthDate(dto.getBirthDate());
        actor.setDeathDate(dto.getDeathDate());
        actor.setBiography(dto.getBiography());

        return actorMapper.toDTO(actorRepository.save(actor));
    }

    /**
     * 5. QİSMİ YENİLƏMƏ (PATCH)
     * Məqsəd: Aktyorun yalnız bir və ya bir neçə sahəsini (məsələn, yalnız bioqrafiyasını) dəyişmək.
     * İş prinsipi: Göndərilən DTO-da hansı sahə 'null' deyilsə, yalnız həmin sahəni yeniləyir,
     * qalan məlumatlara isə toxunmur.
     */
    @Transactional
    public ActorDTO patchActor(Integer id, ActorDTO dto) {
        log.info("Patching actor with id: {}", id);
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ActorNotFoundException("Actor not found with id: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            String trimmedName = dto.getName().trim();
            if (!actor.getName().equalsIgnoreCase(trimmedName) &&
                    actorRepository.existsByNameIgnoreCase(trimmedName)) {
                throw new RuntimeException("Bu adda aktyor artıq mövcuddur!");
            }
            actor.setName(trimmedName);
        }

        if (dto.getBiography() != null) {
            actor.setBiography(dto.getBiography());
        }

        if (dto.getBirthDate() != null) {
            actor.setBirthDate(dto.getBirthDate());
        }

        if (dto.getDeathDate() != null) {
            actor.setDeathDate(dto.getDeathDate());
        }

        // Yenilənmədən sonrakı son tarixləri yoxla
        validateDates(actor.getBirthDate(), actor.getDeathDate());

        return actorMapper.toDTO(actorRepository.save(actor));
    }

    /**
     * 6. AKTYORU SİLMƏK
     * Məqsəd: Aktyorun bazadakı qeydini tamamilə yox etmək.
     * İş prinsipi: Silməzdən əvvəl həmin aktyorun mövcudluğunu yoxlayır,
     * yoxdursa xəta verir, varsa silmə əməliyyatını icra edir.
     */
    @Transactional
    public void deleteActor(Integer id) {
        log.info("Deleting actor with id: {}", id);
        if (!actorRepository.existsById(id)) {
            throw new ActorNotFoundException("Actor not found with id: " + id);
        }
        actorRepository.deleteById(id);
    }

    /**
     * 7. DİNAMİK AXtARIŞ (Search)
     * Məqsəd: Verilən filtr şərtlərinə (ad, doğum ili və s.) uyğun aktyorları tapmaq.
     * İş prinsipi: 'ActorFilter' obyektinə yığılmış axtarış şərtlərini 'ActorSpecification'
     * vasitəsilə mürəkkəb bir SQL sorğusuna çevirib icra edir.
     */
    public List<ActorDTO> search(ActorFilter filter) {
        log.info("Searching actors with criteria: {}", filter);
        List<Actor> actors = actorRepository.findAll(ActorSpecification.getSpecification(filter));
        return actorMapper.toDTOList(actors);
    }

    /**
     * KÖMƏKÇİ METOD: TARİX YOXLANILMASI
     * Bu metod ölüm tarixinin doğumdan əvvəl olmamasını təmin edir.
     * Məsələn: 1990-da doğulub 1980-də ölmək mümkün deyil.
     */
    private void validateDates(java.time.LocalDate birth, java.time.LocalDate death) {
        if (birth != null && death != null && death.isBefore(birth)) {
            throw new IllegalArgumentException("Ölüm tarixi doğum tarixindən əvvəl ola bilməz!");
        }
    }
}