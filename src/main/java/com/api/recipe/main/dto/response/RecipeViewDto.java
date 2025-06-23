package com.api.recipe.main.dto.response;

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

    private List<String> ingredients;

    private String instruction;

    private boolean vegetarian;

    private int servings;
}
