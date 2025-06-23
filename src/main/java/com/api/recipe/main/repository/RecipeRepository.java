package com.api.recipe.main.repository;

import com.api.recipe.common.repository.BaseRepository;
import com.api.recipe.main.entity.Recipe;
import com.api.recipe.main.projection.RecipeViewProjection;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipeRepository extends BaseRepository<Recipe> {
    Optional<Recipe> findEntityByUuid(UUID uuid);

    Optional<RecipeViewProjection> findByUuid(UUID uuid);
}
