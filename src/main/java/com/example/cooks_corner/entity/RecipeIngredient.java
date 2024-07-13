package com.example.cooks_corner.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipe_ingredient_junction")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, name = "measure_unit")
    private String measureUnit;

    @ManyToOne()
    @JoinColumn(name = "recipe_id")
    @ToString.Exclude
    private Recipe recipe;

    @ManyToOne()
    @JoinColumn(name = "ingredient_id")
    @ToString.Exclude
    private Ingredient ingredient;

    public RecipeIngredient(double amount, String measureUnit, Recipe recipe, Ingredient ingredient) {
        this.amount = amount;
        this.measureUnit = measureUnit;
        this.recipe = recipe;
        this.ingredient = ingredient;
    }
}
