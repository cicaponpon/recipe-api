package com.api.recipe.main.dto.request;

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
    private List<@NotBlank(message = "Ingredient cannot be blank") String> ingredients;

    @NotBlank(message = "Instruction is required")
    private String instruction;

    @NotNull(message = "Vegetarian flag is required")
    private boolean vegetarian;

    @Min(value = 1, message = "Servings must be at least 1")
    private int servings;
}
