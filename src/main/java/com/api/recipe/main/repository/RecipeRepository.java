package com.api.recipe.main.repository;

import com.api.recipe.common.repository.BaseRepository;
import com.api.recipe.main.entity.Recipe;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends BaseRepository<Recipe> {
}
