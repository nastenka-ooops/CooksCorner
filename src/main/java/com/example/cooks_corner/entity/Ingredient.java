package com.example.cooks_corner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
}
