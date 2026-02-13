package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.entity.Role;
import org.example.moviesplatform.error.model.RoleNotFoundException;
import org.example.moviesplatform.model.RoleFilter; // Filteri import etdik
import org.example.moviesplatform.repository.RoleRepository;
import org.example.moviesplatform.specification.RoleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    // ---------- GET ALL (List) ----------
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        log.debug("Bütün rollar siyahı şəklində gətirilir");
        return roleRepository.findAll();
    }

    // ---------- GET BY ID ----------
    @Transactional(readOnly = true)
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role id=" + id + " tapılmadı"));
    }

    // ---------- GET BY NAME ----------
    @Transactional(readOnly = true)
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Rol adı tapılmadı: " + name));
    }

    // ---------- GET ALL (Paginated & Filtered) ----------
    @Transactional(readOnly = true)
    public Page<Role> getAllRoles(RoleFilter filter, Pageable pageable) {
        log.debug("Rollar filtrlənir: {}, səhifə: {}", filter, pageable);

        // BURANI DƏYİŞDİK: Specification yaradırıq
        RoleSpecification spec = new RoleSpecification(filter);

        // Repository-də olan standart findAll(Specification, Pageable) metodunu çağırırıq
        return roleRepository.findAll(spec, pageable);
    }

    // ---------- CREATE ----------
    @Transactional(propagation = Propagation.REQUIRED)
    public Role addRole(Role role) {
        // Dublikat yoxlaması: Eyni adda rol bazada olmamalıdır
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Bu adda rol artıq mövcuddur: " + role.getName());
        }

        Role saved = roleRepository.save(role);
        log.info("Yeni rol yaradıldı: {}", saved.getName());
        return saved;
    }

    // ---------- PARTIAL UPDATE (PATCH) ----------
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Role update(Integer roleId, Role rolePayload) {
        Role role = getRoleById(roleId);

        if (rolePayload.getName() != null) {
            // Əgər ad dəyişirsə, yeni adın başqasında olub-olmadığını yoxlamaq olar
            role.setName(rolePayload.getName());
        }
        if (rolePayload.getDescription() != null) {
            role.setDescription(rolePayload.getDescription());
        }

        log.info("Rol qismən yeniləndi: id={}", roleId);
        return roleRepository.save(role);
    }

    // ---------- DELETE ----------
    @Transactional
    public void delete(Integer id) {
        Role role = getRoleById(id);

        // Kritik xəbərdarlıq: Əgər bu rola bağlı user-lər varsa, silinməyə icazə verməmək olar
        if (role.getUserEntities() != null && !role.getUserEntities().isEmpty()) {
            throw new RuntimeException("Bu rola bağlı istifadəçilər var, silmək mümkün deyil!");
        }

        roleRepository.delete(role);
        log.info("Rol silindi: id={}", id);
    }
}