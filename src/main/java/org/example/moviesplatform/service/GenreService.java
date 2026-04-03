package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.dto.GenreDTO;
import org.example.moviesplatform.entity.Genre;
import org.example.moviesplatform.error.model.GenreNotFoundException;
import org.example.moviesplatform.error.model.ResourceAlreadyExistsException;
import org.example.moviesplatform.mapper.GenreMapper;
import org.example.moviesplatform.model.GenreFilter;
import org.example.moviesplatform.repository.GenreRepository;
import org.example.moviesplatform.specification.GenreSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    /**
     * 1. BÜTÜN JANRLARI GƏTİRMƏK
     */
    public List<GenreDTO> getAll() {
        log.info("Bütün janrlar gətirilir");
        return genreMapper.toDTOList(genreRepository.findAll());
    }

    /**
     * 2. ID-YƏ GÖRƏ JANR TAPMAQ
     */
    public GenreDTO getById(Integer id) {
        log.info("ID-si {} olan janr axtarılır", id);
        return genreRepository.findById(id)
                .map(genreMapper::toDTO)
                .orElseThrow(() -> new GenreNotFoundException("Bu ID-li janr tapılmadı: " + id));
    }

    /**
     * 3. YENİ JANR YARATMAQ
     */
    @Transactional
    public GenreDTO create(GenreDTO dto) {
        String cleanName = dto.getName().trim();

        if (genreRepository.existsByNameIgnoreCase(cleanName)) {
            throw new ResourceAlreadyExistsException("Bu janr artıq sistemdə mövcuddur: " + cleanName);
        }

        dto.setName(cleanName);
        Genre genre = genreMapper.toEntity(dto);
        Genre saved = genreRepository.save(genre);
        log.info("Yeni janr yaradıldı: {}", saved.getName());
        return genreMapper.toDTO(saved);
    }

    /**
     * 4. JANRI YENİLƏMƏK (UPDATE)
     */
    @Transactional
    public GenreDTO update(Integer id, GenreDTO dto) {
        log.info("ID-si {} olan janr yenilənir", id);
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Yeniləmək üçün janr tapılmadı: " + id));

        String cleanName = dto.getName().trim(); // DÜZƏLİŞ

        if (!genre.getName().equalsIgnoreCase(cleanName) &&
                genreRepository.existsByNameIgnoreCase(cleanName)) {
            throw new ResourceAlreadyExistsException("Bu adda digər bir janr artıq mövcuddur!");
        }

        genre.setName(cleanName);
        return genreMapper.toDTO(genreRepository.save(genre));
    }

    /**
     * 5. JANRI SİLMƏK
     */
    @Transactional
    public void delete(Integer id) {
        if (!genreRepository.existsById(id)) {
            throw new GenreNotFoundException("Silinəcək janr tapılmadı: " + id);
        }
        genreRepository.deleteById(id);
        log.info("Janr silindi. ID: {}", id); // ID-ni mesajda deyil, logda saxlayırıq
    }

    /**
     * 6. DİNAMİK AXTARIŞ (SEARCH)
     */
    public List<GenreDTO> search(GenreFilter filter) {
        log.info("Janrlar üzrə filtrli axtarış: {}", filter);
        List<Genre> genres = genreRepository.findAll(GenreSpecification.getSpecification(filter));
        return genreMapper.toDTOList(genres);
    }
}