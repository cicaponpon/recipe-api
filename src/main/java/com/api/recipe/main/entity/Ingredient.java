package com.api.recipe.main.entity;

import com.api.recipe.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "ingredient")
public class Ingredient extends BaseEntity {
    public static class Fields {
        public static final String NAME = "name";
        public static final String RECIPE_ID = "recipe_id";

        private Fields() {
        }
    }

    @Column(name = Fields.NAME, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = Fields.RECIPE_ID, nullable = false)
    private Recipe recipe;
}
