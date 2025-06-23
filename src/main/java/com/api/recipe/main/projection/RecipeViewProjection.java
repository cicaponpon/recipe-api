package com.api.recipe.main.projection;

import com.api.recipe.main.entity.Ingredient;
import com.api.recipe.main.entity.Recipe;
import org.springframework.data.rest.core.config.Projection;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Projection(name = "recipeViewProjection", types = {Recipe.class})
public interface RecipeViewProjection {
    UUID getUuid();

    OffsetDateTime getCreatedAt();

    String getTitle();

    String getDescription();

    List<Ingredient> getIngredients();

    String getInstruction();

    boolean isVegetarian();

    int getServings();
}
