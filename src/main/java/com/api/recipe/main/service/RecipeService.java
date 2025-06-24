package com.api.recipe.main.service;

import com.api.recipe.common.service.TranslatorService;
import com.api.recipe.main.dto.request.RecipeRequestDto;
import com.api.recipe.main.dto.response.RecipeCreatedDto;
import com.api.recipe.main.dto.response.RecipeUpdatedDto;
import com.api.recipe.main.dto.response.RecipeViewDto;
import com.api.recipe.main.entity.Ingredient;
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
    private final TranslatorService translatorService;

    /**
     * Create new recipe
     */
    @Transactional
    public RecipeCreatedDto createRecipe(RecipeRequestDto recipeRequestDto) {
        // Set new recipe
        Recipe recipe = new Recipe();
        BeanUtils.copyProperties(recipeRequestDto, recipe);
        List<Ingredient> ingredients = recipeRequestDto.toIngredientEntities(recipe);
        recipe.setIngredients(ingredients);

        // Save new recipe
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Return created recipe DTO
        RecipeCreatedDto recipeCreatedDto = new RecipeCreatedDto();
        BeanUtils.copyProperties(savedRecipe, recipeCreatedDto);
        return recipeCreatedDto;
    }

    /**
     * Get recipe by UUID
     */
    public RecipeViewDto getRecipe(UUID uuid) {
        // Find recipe
        RecipeViewProjection recipeViewProjection = recipeRepository.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        getRecipeNotFoundMessage(uuid)
                ));

        RecipeViewDto recipeViewDto = new RecipeViewDto();
        BeanUtils.copyProperties(recipeViewProjection, recipeViewDto);
        recipeViewDto.setIngredients(RecipeViewDto.fromIngredients(recipeViewProjection.getIngredients()));
        return recipeViewDto;
    }

    /**
     * Update recipe by UUID
     */
    @Transactional
    public RecipeUpdatedDto updateRecipe(RecipeRequestDto recipeRequestDto, UUID uuid) {
        // Find recipe
        Recipe recipe = recipeRepository.findEntityByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        getRecipeNotFoundMessage(uuid)
                ));
        BeanUtils.copyProperties(recipeRequestDto, recipe);

        // Clear and update ingredients
        recipe.getIngredients().clear();
        List<Ingredient> updatedIngredients = recipeRequestDto.toIngredientEntities(recipe);
        recipe.getIngredients().addAll(updatedIngredients);

        // Save updated recipe
        Recipe savedRecipe = recipeRepository.save(recipe);

        // Return updated recipe DTO
        RecipeUpdatedDto recipeUpdatedDto = new RecipeUpdatedDto();
        BeanUtils.copyProperties(savedRecipe, recipeUpdatedDto);
        return recipeUpdatedDto;
    }

    /**
     * Delete recipe by uuid
     */
    @Transactional
    public void deleteRecipe(UUID uuid) {
        Recipe recipe = recipeRepository.findEntityByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        getRecipeNotFoundMessage(uuid)
                ));

        recipeRepository.delete(recipe);
    }

    /**
     * Search recipes by:
     * - Vegetarian (true/false)
     * - Number of servings
     * - Included ingredients (supports partial name matching)
     * - Excluded ingredients (supports partial name matching)
     * - Instruction content (supports partial keyword search)
     */
    public Page<RecipeViewDto> searchRecipes(Boolean vegetarian,
                                             Integer servings,
                                             List<String> includedIngredients,
                                             List<String> excludedIngredients,
                                             String instruction,
                                             Pageable pageable) {

        // Define recipe search specification
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

        // Fetch paginated recipe
        Page<Recipe> recipePage = recipeRepository.findAll(recipeSpecification, pageable);

        return recipePage.map(this::convertToRecipeViewDto);
    }

    // Map Recipe entity to view dto
    private RecipeViewDto convertToRecipeViewDto(Recipe recipe) {
        RecipeViewDto dto = new RecipeViewDto();
        BeanUtils.copyProperties(recipe, dto);
        dto.setIngredients(RecipeViewDto.fromIngredients(recipe.getIngredients()));
        return dto;
    }

    private String getRecipeNotFoundMessage(UUID uuid) {
        return translatorService.process("error.recipe.not.found", new Object[]{uuid});
    }
}
