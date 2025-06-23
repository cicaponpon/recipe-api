package com.api.recipe.main.controller;

import com.api.recipe.common.dto.response.ApiResponse;
import com.api.recipe.main.dto.request.RecipeRequestDto;
import com.api.recipe.main.dto.response.RecipeCreatedDto;
import com.api.recipe.main.dto.response.RecipeViewDto;
import com.api.recipe.main.dto.response.RecipeUpdatedDto;
import com.api.recipe.main.service.RecipeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    /**
     * CREATE RECIPE API
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RecipeCreatedDto>> createRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto) {
        RecipeCreatedDto recipeCreatedDto = recipeService.createRecipe(recipeRequestDto);
        ApiResponse<RecipeCreatedDto> response = new ApiResponse<>(
                true,
                "Recipe created successfully",
                recipeCreatedDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET RECIPE API
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<RecipeViewDto>> getRecipe(@PathVariable UUID uuid) {
        RecipeViewDto recipeViewDto = recipeService.getRecipe(uuid);
        ApiResponse<RecipeViewDto> response = new ApiResponse<>(
                true,
                "Recipe retrieved successfully",
                recipeViewDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * UPDATE RECIPE API
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<RecipeUpdatedDto>> updateRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto,
                                                                      @PathVariable UUID uuid) {
        RecipeUpdatedDto recipeUpdatedDto = recipeService.updateRecipe(recipeRequestDto, uuid);
        ApiResponse<RecipeUpdatedDto> response = new ApiResponse<>(
                true,
                "Recipe updated successfully",
                recipeUpdatedDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * DELETE RECIPE API
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Null>> deleteRecipe(@PathVariable UUID uuid) {
        recipeService.deleteRecipe(uuid);
        ApiResponse<Null> response = new ApiResponse<>(
                true,
                "Recipe deleted successfully"
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * SEARCH RECIPE API
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RecipeViewDto>>> searchRecipes(
            @RequestParam(required = false) Boolean vegetarian,
            @RequestParam(required = false) Integer servings,
            @RequestParam(required = false) List<String> includedIngredients,
            @RequestParam(required = false) List<String> excludedIngredients,
            @RequestParam(required = false) String instruction,
            @PageableDefault Pageable pageable) {
        Page<RecipeViewDto> results = recipeService.searchRecipes(
                vegetarian, servings, includedIngredients, excludedIngredients, instruction, pageable
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Search successful", results));
    }
}
