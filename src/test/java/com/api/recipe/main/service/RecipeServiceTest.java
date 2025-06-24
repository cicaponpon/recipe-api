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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private TranslatorService translatorService;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static RecipeRequestDto getRecipeRequestDto() {
        RecipeRequestDto requestDto = new RecipeRequestDto();
        requestDto.setTitle("Chicken Adobo");
        requestDto.setDescription("Classic Filipino chicken dish simmered in soy sauce, vinegar, and garlic.");
        requestDto.setInstruction("""
                    1. Heat oil in a pan over medium heat.
                    2. Sauté garlic and onions until fragrant.
                    3. Add chicken and cook until lightly browned.
                    4. Pour in soy sauce and vinegar. Do not stir.
                    5. Add bay leaves and peppercorns.
                    6. Bring to a boil, then lower heat and simmer for 30–40 minutes until chicken is tender.
                    7. Optional: Reduce sauce to thicken.
                    8. Serve hot with rice.
                """.trim());
        requestDto.setVegetarian(false);
        requestDto.setServings(4);

        RecipeRequestDto.IngredientRequestDto chicken = new RecipeRequestDto.IngredientRequestDto();
        chicken.setName("1 kg chicken thighs or drumsticks");

        RecipeRequestDto.IngredientRequestDto soySauce = new RecipeRequestDto.IngredientRequestDto();
        soySauce.setName("1/2 cup soy sauce");

        RecipeRequestDto.IngredientRequestDto vinegar = new RecipeRequestDto.IngredientRequestDto();
        vinegar.setName("1/2 cup vinegar");

        RecipeRequestDto.IngredientRequestDto garlic = new RecipeRequestDto.IngredientRequestDto();
        garlic.setName("6 cloves garlic, minced");

        RecipeRequestDto.IngredientRequestDto bayLeaves = new RecipeRequestDto.IngredientRequestDto();
        bayLeaves.setName("3 bay leaves");

        RecipeRequestDto.IngredientRequestDto peppercorns = new RecipeRequestDto.IngredientRequestDto();
        peppercorns.setName("1 tsp whole peppercorns");

        RecipeRequestDto.IngredientRequestDto onions = new RecipeRequestDto.IngredientRequestDto();
        onions.setName("1 onion, sliced");

        RecipeRequestDto.IngredientRequestDto oil = new RecipeRequestDto.IngredientRequestDto();
        oil.setName("2 tbsp cooking oil");

        requestDto.setIngredients(List.of(
                chicken, soySauce, vinegar, garlic, bayLeaves, peppercorns, onions, oil
        ));

        return requestDto;
    }

    @Test
    void createRecipe_success() {
        RecipeRequestDto recipeRequestDto = getRecipeRequestDto();

        UUID uuid = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();

        Recipe savedRecipe = new Recipe();
        savedRecipe.setUuid(uuid);
        savedRecipe.setCreatedAt(createdAt);
        savedRecipe.setTitle(recipeRequestDto.getTitle());

        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        RecipeCreatedDto result = recipeService.createRecipe(recipeRequestDto);

        assertNotNull(result);
        assertEquals(recipeRequestDto.getTitle(), result.getTitle());
        assertNotNull(result.getUuid());
        assertNotNull(result.getCreatedAt());
        verify(recipeRepository).save(any());
    }

    @Test
    void updateRecipe_success() {
        RecipeRequestDto recipeRequestDto = getRecipeRequestDto();

        UUID uuid = UUID.randomUUID();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        Recipe existingRecipe = new Recipe();
        existingRecipe.setIngredients(new ArrayList<>());

        Recipe savedRecipe = new Recipe();
        savedRecipe.setUuid(uuid);
        savedRecipe.setUpdatedAt(updatedAt);
        savedRecipe.setTitle(recipeRequestDto.getTitle());

        when(recipeRepository.findEntityByUuid(uuid)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

        RecipeUpdatedDto result = recipeService.updateRecipe(recipeRequestDto, uuid);

        assertNotNull(result);
        assertEquals(recipeRequestDto.getTitle(), result.getTitle());
        assertNotNull(result.getUuid());
        assertNotNull(result.getUpdatedAt());
        verify(recipeRepository).save(any());
    }

    @Test
    void getRecipe_found() {
        UUID uuid = UUID.randomUUID();
        RecipeViewProjection projection = mock(RecipeViewProjection.class);

        when(recipeRepository.findByUuid(uuid)).thenReturn(Optional.of(projection));
        when(projection.getIngredients()).thenReturn(Collections.emptyList());

        RecipeViewDto result = recipeService.getRecipe(uuid);
        assertNotNull(result);
        verify(recipeRepository).findByUuid(uuid);
    }

    @Test
    void getRecipe_notFound_throwsException() {
        UUID uuid = UUID.randomUUID();
        when(recipeRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        when(translatorService.process(anyString(), (Locale) any())).thenReturn("Not found");

        assertThrows(EntityNotFoundException.class, () -> recipeService.getRecipe(uuid));
    }

    @Test
    void deleteRecipe_success() {
        UUID uuid = UUID.randomUUID();
        Recipe recipe = new Recipe();
        when(recipeRepository.findEntityByUuid(uuid)).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipe(uuid);
        verify(recipeRepository).delete(recipe);
    }

    @Test
    void searchRecipes_emptyResult() {
        @SuppressWarnings("unchecked")
        Specification<Recipe> anySpec = (Specification<Recipe>) any(Specification.class);

        when(recipeRepository.findAll(anySpec, any(PageRequest.class)))
                .thenReturn(Page.empty());

        Page<RecipeViewDto> result = recipeService.searchRecipes(
                null, null, null, null, null, PageRequest.of(0, 10));

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private Page<Recipe> prepareMockRecipes() {
        Recipe vegetarianRecipe = new Recipe();
        vegetarianRecipe.setTitle("Vegetarian Dish");
        vegetarianRecipe.setVegetarian(true);
        vegetarianRecipe.setServings(2);
        vegetarianRecipe.setInstruction("Boil gently.");

        Ingredient garlic = new Ingredient();
        garlic.setName("1 Cabbage");
        vegetarianRecipe.setIngredients(List.of(garlic));

        Recipe nonVegetarianRecipe = new Recipe();
        nonVegetarianRecipe.setTitle("Non-Vegetarian Dish");
        nonVegetarianRecipe.setVegetarian(false);
        nonVegetarianRecipe.setServings(5);
        nonVegetarianRecipe.setInstruction("Grill heavily.");

        Ingredient beef = new Ingredient();
        beef.setName("2kg Beef");
        nonVegetarianRecipe.setIngredients(List.of(beef));

        return new PageImpl<>(List.of(vegetarianRecipe, nonVegetarianRecipe), PageRequest.of(0, 10), 2);
    }


    @Test
    void searchRecipes_withVegetarianTrue_shouldReturnMatchingOnly() {
        List<Recipe> allRecipes = prepareMockRecipes().getContent();

        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    Pageable pageable = invocation.getArgument(1);

                    List<Recipe> filtered = allRecipes.stream()
                            .filter(recipe -> recipe.getVegetarian() != null && recipe.getVegetarian())
                            .toList();

                    return new PageImpl<>(filtered, pageable, filtered.size());
                });

        Page<RecipeViewDto> result = recipeService.searchRecipes(true, null, null,
                null, null, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getVegetarian()).isTrue();
    }

    @Test
    void searchRecipes_withServings_shouldReturnOnlyMatching() {
        List<Recipe> allRecipes = prepareMockRecipes().getContent();

        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    Pageable pageable = invocation.getArgument(1);
                    List<Recipe> filtered = allRecipes.stream()
                            .filter(recipe -> recipe.getServings() == 2)
                            .toList();
                    return new PageImpl<>(filtered, pageable, filtered.size());
                });

        Page<RecipeViewDto> result = recipeService.searchRecipes(
                null, 2, null, null, null,
                PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getServings()).isEqualTo(2);
    }

    @Test
    void searchRecipes_withInstruction_shouldMatchKeyword() {
        List<Recipe> allRecipes = prepareMockRecipes().getContent();

        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    Pageable pageable = invocation.getArgument(1);
                    String keyword = "boil";

                    List<Recipe> filtered = allRecipes.stream()
                            .filter(recipe -> recipe.getInstruction() != null &&
                                    recipe.getInstruction().toLowerCase().contains(keyword))
                            .toList();

                    return new PageImpl<>(filtered, pageable, filtered.size());
                });

        Page<RecipeViewDto> result = recipeService.searchRecipes(
                null, null, null, null, "boil", PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getInstruction().toLowerCase()).contains("boil");
    }

    @Test
    void searchRecipes_withIncludedIngredient_shouldReturnMatching() {
        List<Recipe> allRecipes = prepareMockRecipes().getContent();
        List<String> included = List.of("cabbage");

        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    Pageable pageable = invocation.getArgument(1);

                    List<Recipe> filtered = allRecipes.stream()
                            .filter(recipe -> recipe.getIngredients().stream()
                                    .anyMatch(ing ->
                                            included.stream().anyMatch(filter ->
                                                    ing.getName().toLowerCase().contains(filter.toLowerCase())
                                            )
                                    ))
                            .toList();

                    return new PageImpl<>(filtered, pageable, filtered.size());
                });

        Page<RecipeViewDto> result = recipeService.searchRecipes(
                null, null, included, null, null, PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getIngredients().get(0).getName().toLowerCase()).contains("cabbage");
    }

    @Test
    void searchRecipes_withExcludedIngredient_shouldExcludeMatching() {
        List<Recipe> allRecipes = prepareMockRecipes().getContent();
        List<String> excluded = List.of("beef");

        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    Pageable pageable = invocation.getArgument(1);

                    List<Recipe> filtered = allRecipes.stream()
                            .filter(recipe -> recipe.getIngredients().stream()
                                    .noneMatch(ing ->
                                            excluded.stream().anyMatch(filter ->
                                                    ing.getName().toLowerCase().contains(filter.toLowerCase())
                                            )
                                    ))
                            .toList();

                    return new PageImpl<>(filtered, pageable, filtered.size());
                });

        Page<RecipeViewDto> result = recipeService.searchRecipes(
                null, null, null, excluded, null, PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Vegetarian Dish");
    }

    @Test
    void searchRecipes_withAllFilters_shouldReturnStrictMatch() {
        List<Recipe> allRecipes = prepareMockRecipes().getContent();

        Boolean vegetarian = true;
        Integer servings = 2;
        List<String> included = List.of("cabbage");
        List<String> excluded = List.of("beef");
        String instruction = "boil";

        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenAnswer(invocation -> {
                    Pageable pageable = invocation.getArgument(1);

                    List<Recipe> filtered = allRecipes.stream()
                            .filter(r -> Boolean.TRUE.equals(r.getVegetarian()))
                            .filter(r -> Objects.equals(r.getServings(), servings))
                            .filter(r -> r.getInstruction().toLowerCase().contains(instruction))
                            .filter(r -> r.getIngredients().stream()
                                    .anyMatch(i -> included.stream()
                                            .anyMatch(inc -> i.getName().toLowerCase().contains(inc))))
                            .filter(r -> r.getIngredients().stream()
                                    .noneMatch(i -> excluded.stream()
                                            .anyMatch(exc -> i.getName().toLowerCase().contains(exc))))
                            .toList();

                    return new PageImpl<>(filtered, pageable, filtered.size());
                });

        Page<RecipeViewDto> result = recipeService.searchRecipes(
                vegetarian, servings, included, excluded, instruction, PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Vegetarian Dish");
    }
}
