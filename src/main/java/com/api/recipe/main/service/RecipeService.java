package com.api.recipe.main.service;

import com.api.recipe.main.dto.request.RecipeRequestDto;
import com.api.recipe.main.dto.response.RecipeCreatedDto;
import com.api.recipe.main.dto.response.RecipeUpdatedDto;
import com.api.recipe.main.dto.response.RecipeViewDto;
import com.api.recipe.main.entity.Recipe;
import com.api.recipe.main.projection.RecipeViewProjection;
import com.api.recipe.main.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.api.recipe.main.specification.RecipeSpecification.*;

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

    public RecipeViewDto getRecipe(UUID uuid) {
        RecipeViewProjection recipeViewProjection = recipeRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with UUID: " + uuid));

        RecipeViewDto recipeViewDto = new RecipeViewDto();
        BeanUtils.copyProperties(recipeViewProjection, recipeViewDto);
        return recipeViewDto;
    }

    @Transactional
    public RecipeUpdatedDto updateRecipe(RecipeRequestDto recipeRequestDto, UUID uuid) {
        Recipe recipe = recipeRepository.findEntityByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with UUID: " + uuid));
        BeanUtils.copyProperties(recipeRequestDto, recipe);
        Recipe recipeSaved = recipeRepository.save(recipe);

        RecipeUpdatedDto recipeUpdatedDto = new RecipeUpdatedDto();
        BeanUtils.copyProperties(recipeSaved, recipeUpdatedDto);
        return recipeUpdatedDto;
    }

    @Transactional
    public void deleteRecipe(UUID uuid) {
        Recipe recipe = recipeRepository.findEntityByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with UUID: " + uuid));

        recipeRepository.delete(recipe);
    }

    public Page<RecipeViewDto> searchRecipes(Boolean vegetarian,
                                             Integer servings,
                                             List<String> includedIngredients,
                                             List<String> excludedIngredients,
                                             String instruction,
                                             Pageable pageable) {

        Specification<Recipe> recipeSpecification = (root, query, cb) -> {
            query.distinct(true);
            return cb.conjunction();
        };

        recipeSpecification = recipeSpecification
                .and(isVegetarian(vegetarian))
                .and(hasServings(servings))
                .and(containsInstruction(instruction))
                .and(includeIngredients(includedIngredients))
                .and(excludeIngredients(excludedIngredients));

        Page<Recipe> recipePage = recipeRepository.findAll(recipeSpecification, pageable);

        return recipePage.map(recipe -> {
            RecipeViewDto recipeViewDto = new RecipeViewDto();
            BeanUtils.copyProperties(recipe, recipeViewDto);
            return recipeViewDto;
        });
    }


}
