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

    @NotBlank(message = "{recipe.title.required}")
    private String title;

    @NotBlank(message = "{recipe.description.required}")
    private String description;

    @NotEmpty(message = "{recipe.ingredients.required}")
    private List<@Valid IngredientRequestDto> ingredients;

    @NotBlank(message = "{recipe.instruction.required}")
    private String instruction;

    @NotNull(message = "{recipe.vegetarian.required}")
    private Boolean vegetarian;

    @NotNull(message = "{recipe.servings.required}")
    @Min(value = 1, message = "{recipe.servings.min}")
    private Integer servings;

    @Data
    public static class IngredientRequestDto {

        @NotBlank(message = "{ingredient.name.required}")
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
