package org.example.moviesplatform.repository;

import org.example.moviesplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByUsername(String username);
    List<User> findByLastName(String lastName);
}
