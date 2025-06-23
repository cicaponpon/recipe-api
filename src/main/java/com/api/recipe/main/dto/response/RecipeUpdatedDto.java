package com.api.recipe.main.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class RecipeUpdatedDto {
    private UUID uuid;

    private OffsetDateTime updatedAt;

    private String title;
}
