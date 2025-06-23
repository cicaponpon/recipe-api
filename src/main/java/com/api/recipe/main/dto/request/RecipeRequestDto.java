package com.api.recipe.main.dto.request;

import com.api.recipe.main.entity.Ingredient;
import com.api.recipe.main.entity.Recipe;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RecipeRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotEmpty(message = "Ingredients list must not be empty")
    private List<@Valid IngredientRequestDto> ingredients;

    @NotBlank(message = "Instruction is required")
    private String instruction;

    @NotNull(message = "Vegetarian flag is required")
    private Boolean vegetarian;

    @NotNull(message = "Servings is required")
    @Min(value = 1, message = "Servings must be at least 1")
    private Integer servings;

    @Data
    public static class IngredientRequestDto {

        @NotBlank(message = "Ingredient name is required")
        private String name;
    }

    /**
     * Converts ingredient DTOs into entities with recipe reference
     */
    public List<Ingredient> toIngredientEntities(Recipe recipe) {
        return ingredients.stream()
                .map(dto -> {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(dto.getName());
                    ingredient.setRecipe(recipe);
                    return ingredient;
                })
                .toList();
    }

}
