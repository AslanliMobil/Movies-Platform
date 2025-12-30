package org.example.moviesplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.moviesplatform.entity.Role;
import org.example.moviesplatform.error.model.RoleNotFoundException;
import org.example.moviesplatform.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${application.whitelist.role:1}")
    public Long whitelistRoleId;

    private final RoleRepository roleRepository;

    // ---------- GET ALL ----------
    public List<Role> getAllRoles() {
        log.debug("Getting all roles");
        return roleRepository.findAll();
    }

    // ---------- PAGINATION SUPPORT ----------
    public Page<Role> getAllRoles(Pageable pageable) {
        log.debug("Getting paginated roles, pageable={}", pageable);
        return roleRepository.findAll(pageable);
    }

    // ---------- GET BY ID ----------
    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }

    // ---------- GET BY NAME ----------
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }

    // ---------- CREATE ----------
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Role addRole(Role role) {
        Role saved = roleRepository.save(role);
        log.info("Added role: {}", saved);
        return saved;
    }

    // ---------- PARTIAL UPDATE ----------
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Role update(Integer roleId, Role rolePayload) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        // null-checks: yalnız gələn dəyərləri set edirik
        if (rolePayload.getName() != null) {
            role.setName(rolePayload.getName());
        }
        if (rolePayload.getDescription() != null) {
            role.setDescription(rolePayload.getDescription());
        }

        Role saved = roleRepository.save(role);
        log.info("Updated role id={} -> {}", roleId, saved);
        return saved;
    }

    // ---------- FULL UPDATE (PUT) ----------
    @Transactional
    public Role updateRoleFully(Integer id, Role newRole) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        // Tam yeniləmə: bütün sahələri dəyişdiririk
        role.setName(newRole.getName());
        role.setDescription(newRole.getDescription());
        // createdAt / updatedAt sahələrini əgər lazımdırsa burada idarə et

        Role saved = roleRepository.save(role);
        log.info("Fully updated role id={} -> {}", id, saved);
        return saved;
    }

    // ---------- DELETE ----------
    @Transactional
    public void delete(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
        roleRepository.delete(role);
        log.info("Deleted role id={}", id);
    }
}
