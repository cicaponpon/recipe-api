package com.api.recipe.main.service;

import com.api.recipe.main.dto.request.RecipeRequestDto;
import com.api.recipe.main.dto.response.RecipeCreatedDto;
import com.api.recipe.main.entity.Recipe;
import com.api.recipe.main.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Transactional
    public RecipeCreatedDto createRecipe(RecipeRequestDto recipeRequestDto) {
        Recipe recipe = new Recipe();
        BeanUtils.copyProperties(recipeRequestDto, recipe);
        Recipe recipeSaved = recipeRepository.save(recipe);

        RecipeCreatedDto recipeCreatedDto = new RecipeCreatedDto();
        BeanUtils.copyProperties(recipeSaved, recipeCreatedDto);
        return recipeCreatedDto;
    }
}
