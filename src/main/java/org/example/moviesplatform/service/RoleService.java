package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.entity.Role;
import org.example.moviesplatform.error.model.ResourceAlreadyExistsException;
import org.example.moviesplatform.error.model.RoleNotFoundException;
import org.example.moviesplatform.model.RoleFilter;
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

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        log.debug("Bütün rollar siyahı şəklində gətirilir");
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role id=" + id + " tapılmadı"));
    }

    @Transactional(readOnly = true)
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Rol adı tapılmadı: " + name));
    }

    @Transactional(readOnly = true)
    public Page<Role> getAllRoles(RoleFilter filter, Pageable pageable) {
        log.debug("Rollar filtrlənir: {}, səhifə: {}", filter, pageable);

        RoleSpecification spec = new RoleSpecification(filter);

        return roleRepository.findAll(spec, pageable);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Role addRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new ResourceAlreadyExistsException("Bu adda rol artıq mövcuddur: " + role.getName());
        }

        Role saved = roleRepository.save(role);
        log.info("Yeni rol yaradıldı: {}", saved.getName());
        return saved;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Role update(Integer roleId, Role rolePayload) {
        Role role = getRoleById(roleId);

        if (rolePayload.getName() != null && !rolePayload.getName().isBlank()) {
            String newName = rolePayload.getName().trim();
            if (!role.getName().equalsIgnoreCase(newName) && roleRepository.existsByName(newName)) {
                throw new ResourceAlreadyExistsException("Bu adda rol artıq mövcuddur: " + newName);
            }
            role.setName(newName);
        }

        if (rolePayload.getDescription() != null) {
            role.setDescription(rolePayload.getDescription());
        }

        log.info("Rol qismən yeniləndi: id={}", roleId);
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Integer id) {
        Role role = getRoleById(id);

        if (role.getUserEntities() != null && !role.getUserEntities().isEmpty()) {
            throw new RuntimeException("Bu rola bağlı istifadəçilər var, silmək mümkün deyil!");
        }

        roleRepository.delete(role);
        log.info("Rol silindi: id={}", id);
    }
}