package com.example.cooks_corner.repository;

import com.example.cooks_corner.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);
    boolean existsByIdAndLikedRecipes_Id(Long userId, Long recipesId);
    boolean existsByIdAndBookmarkedRecipes_id(Long userId, Long recipesId);
}
