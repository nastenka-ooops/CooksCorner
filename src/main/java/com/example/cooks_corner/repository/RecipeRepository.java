package com.example.cooks_corner.repository;

import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCategory(Category category);

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Recipe> searchByQuery(String query);
}
