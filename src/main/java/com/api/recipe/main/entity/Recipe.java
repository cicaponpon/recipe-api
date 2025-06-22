package com.api.recipe.main.entity;

import com.api.recipe.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "recipe")
public class Recipe extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredients", nullable = false)
    private List<String> ingredients;

    @Column(name = "instruction", columnDefinition = "TEXT", nullable = false)
    private String instruction;

    @Column(name = "vegetarian", nullable = false)
    private boolean vegetarian;

    @Column(name = "servings", nullable = false)
    private int servings;
}
