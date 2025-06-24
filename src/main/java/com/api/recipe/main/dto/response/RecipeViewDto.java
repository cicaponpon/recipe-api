package com.api.recipe.main.dto.response;

import com.api.recipe.main.entity.Ingredient;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class RecipeViewDto {
    private UUID uuid;

    private OffsetDateTime createdAt;

    private String title;

    private String description;

    private List<IngredientViewDto> ingredients;

    private String instruction;

    private Boolean vegetarian;

    private Integer servings;

    @Data
    public static class IngredientViewDto {
        private String name;
    }

    /**
     * Creates ingredient DTOs from entities
     */
    public static List<IngredientViewDto> fromIngredients(List<? extends Ingredient> ingredients) {
        return ingredients.stream()
                .map(i -> {
                    IngredientViewDto dto = new IngredientViewDto();
                    dto.setName(i.getName());
                    return dto;
                })
                .toList();
    }

}
