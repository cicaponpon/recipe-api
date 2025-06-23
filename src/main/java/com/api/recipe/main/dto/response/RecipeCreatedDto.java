package com.api.recipe.main.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class RecipeCreatedDto {
    private UUID uuid;

    private OffsetDateTime createdAt;

    private String title;
}
