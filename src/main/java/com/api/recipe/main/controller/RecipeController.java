package com.api.recipe.main.controller;

import com.api.recipe.common.dto.response.ApiResponse;
import com.api.recipe.main.dto.request.RecipeRequestDto;
import com.api.recipe.main.dto.response.RecipeCreatedDto;
import com.api.recipe.main.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

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
}
