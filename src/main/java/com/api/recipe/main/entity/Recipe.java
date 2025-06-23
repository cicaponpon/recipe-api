package com.api.recipe.main.entity;

import com.api.recipe.common.entity.BaseEntity;
import com.api.recipe.common.util.ConstantUtil;
import com.api.recipe.main.dto.response.RecipeViewDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "recipe")
public class Recipe extends BaseEntity {
    public static class Fields {
        public static final String UUID = "uuid";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String INGREDIENTS = "ingredients";
        public static final String INSTRUCTION = "instruction";
        public static final String VEGETARIAN = "vegetarian";
        public static final String SERVINGS = "servings";

        private Fields() {
        }
    }

    @Column(name = Fields.UUID, nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = Fields.TITLE, nullable = false)
    private String title;

    @Column(name = Fields.DESCRIPTION, nullable = false)
    private String description;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;

    @Column(name = Fields.INSTRUCTION, columnDefinition = "TEXT", nullable = false)
    private String instruction;

    @Column(name = Fields.VEGETARIAN, nullable = false)
    private boolean vegetarian;

    @Column(name = Fields.SERVINGS, nullable = false)
    private int servings;

    @SuppressWarnings(ConstantUtil.UNUSED_WARNING)
    @PrePersist
    protected void prePersist() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
