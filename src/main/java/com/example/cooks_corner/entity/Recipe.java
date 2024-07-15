package com.example.cooks_corner.entity;

import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    @Column(nullable = false)
    private int cookingTimeMinutes;

    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Setter
    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<RecipeIngredient> recipeIngredients;

    @ManyToMany
    @JoinTable(
            name = "user_recipe_like",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<AppUser> likes;

    @ManyToMany
    @JoinTable(
            name = "user_recipe_bookmark",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<AppUser> bookmarks;

    public Recipe(String title, String description, Category category, Difficulty difficulty, int cookingTimeMinutes,
                  Image image, AppUser user, Set<RecipeIngredient> recipeIngredients, Set<AppUser> likes,
                  Set<AppUser> bookmarks) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.cookingTimeMinutes = cookingTimeMinutes;
        this.image = image;
        this.user = user;
        this.recipeIngredients = recipeIngredients;
        this.likes = likes;
        this.bookmarks = bookmarks;
    }


}
