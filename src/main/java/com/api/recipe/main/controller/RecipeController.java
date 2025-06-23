package com.api.recipe.main.controller;

import com.api.recipe.common.dto.response.ApiResponse;
import com.api.recipe.common.service.TranslatorService;
import com.api.recipe.main.dto.request.RecipeRequestDto;
import com.api.recipe.main.dto.response.RecipeCreatedDto;
import com.api.recipe.main.dto.response.RecipeUpdatedDto;
import com.api.recipe.main.dto.response.RecipeViewDto;
import com.api.recipe.main.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Locale;
import java.util.UUID;

@Tag(name = "Recipe Controller", description = "Operations related to recipes")
@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final TranslatorService translatorService;

    @Operation(summary = "Create a new recipe", description = "Creates a new recipe by accepting recipe details.")
    @PostMapping
    public ResponseEntity<ApiResponse<RecipeCreatedDto>> createRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto,
                                                                      Locale locale) {
        RecipeCreatedDto recipeCreatedDto = recipeService.createRecipe(recipeRequestDto);
        ApiResponse<RecipeCreatedDto> response = new ApiResponse<>(
                true,
                translatorService.process("recipe.create.success", locale),
                recipeCreatedDto
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "Get recipe by UUID",
            description = "Retrieves a single recipe using its UUID, including full details and list of ingredients."
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<RecipeViewDto>> getRecipe(@PathVariable UUID uuid, Locale locale) {
        RecipeViewDto recipeViewDto = recipeService.getRecipe(uuid);
        ApiResponse<RecipeViewDto> response = new ApiResponse<>(
                true,
                translatorService.process("recipe.get.success", locale),
                recipeViewDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(
            summary = "Update an existing recipe",
            description = "Updates the details of an existing recipe by UUID with new data provided in the request body."
    )
    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<RecipeUpdatedDto>> updateRecipe(@Valid @RequestBody RecipeRequestDto recipeRequestDto,
                                                                      @PathVariable UUID uuid, Locale locale) {
        RecipeUpdatedDto recipeUpdatedDto = recipeService.updateRecipe(recipeRequestDto, uuid);
        ApiResponse<RecipeUpdatedDto> response = new ApiResponse<>(
                true,
                translatorService.process("recipe.update.success", locale),
                recipeUpdatedDto
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Delete a recipe", description = "Deletes a recipe identified by its UUID.")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<Null>> deleteRecipe(@PathVariable UUID uuid, Locale locale) {
        recipeService.deleteRecipe(uuid);
        ApiResponse<Null> response = new ApiResponse<>(
                true,
                translatorService.process("recipe.delete.success", locale)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Search recipes with filters",
            description = "Searches recipes using optional filters: vegetarian flag, number of servings, " +
                    "and partial text matches for included/excluded ingredients and instruction content. " +
                    "Supports pagination."
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RecipeViewDto>>> searchRecipes(
            @RequestParam(required = false) Boolean vegetarian,
            @RequestParam(required = false) Integer servings,
            @RequestParam(required = false) List<String> includedIngredients,
            @RequestParam(required = false) List<String> excludedIngredients,
            @RequestParam(required = false) String instruction,
            @PageableDefault Pageable pageable,
            Locale locale) {
        Page<RecipeViewDto> results = recipeService.searchRecipes(
                vegetarian, servings, includedIngredients, excludedIngredients, instruction, pageable
        );

        String messageKey = results.isEmpty() ? "recipe.search.empty" : "recipe.search.success";

        ApiResponse<Page<RecipeViewDto>> response = new ApiResponse<>(
                true,
                translatorService.process(messageKey, locale),
                results
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
