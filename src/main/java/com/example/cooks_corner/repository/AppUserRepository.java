package com.example.cooks_corner.repository;

import com.example.cooks_corner.entity.AppUser;
import com.example.cooks_corner.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);

    boolean existsByIdAndLikedRecipes_Id(Long userId, Long recipesId);

    boolean existsByIdAndBookmarkedRecipes_id(Long userId, Long recipesId);

    @Query("SELECT r FROM AppUser r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(r.bio) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<AppUser> searchByQuery(String query);
}
