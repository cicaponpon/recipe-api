package com.api.recipe.main.controller;

import com.api.recipe.common.service.TranslatorService;
import com.api.recipe.main.dto.request.RecipeRequestDto;
import com.api.recipe.main.dto.response.RecipeViewDto;
import com.api.recipe.main.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RecipeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RecipeService recipeService;
    private TranslatorService translatorService;

    private static final String BASE_URL = "/api/recipe";
    private static final String VALIDATION_KEY = "error.validation.failed";
    private static final String VALIDATION_MESSAGE = "Validation failed";
    private static final String CREATE_RECIPE_SUCCESS_KEY = "recipe.create.success";
    private static final String CREATE_RECIPE_SUCCESS_MESSAGE = "Recipe created successfully";
    private static final String UPDATE_RECIPE_SUCCESS_KEY = "recipe.update.success";
    private static final String UPDATE_RECIPE_SUCCESS_MESSAGE = "Recipe updated successfully";
    private static final String GET_RECIPE_SUCCESS_KEY = "recipe.get.success";
    private static final String GET_RECIPE_SUCCESS_MESSAGE = "Recipe retrieved successfully";
    private static final String DELETE_RECIPE_SUCCESS_KEY = "recipe.delete.success";
    private static final String DELETE_RECIPE_SUCCESS_MESSAGE = "Recipe deleted successfully";
    private static final String SEARCH_RECIPE_SUCCESS_KEY = "recipe.search.success";
    private static final String SEARCH_RECIPE_SUCCESS_MESSAGE = "Recipes found";
    private static final String SEARCH_RECIPE_EMPTY_KEY = "recipe.search.empty";
    private static final String SEARCH_RECIPE_EMPTY_MESSAGE = "No recipes found";

    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    private static final String INVALID_UUID = "invalid-uuid";
    private static final String VALID_UUID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String INVALID_PARAM_KEY = "error.invalid.parameter";
    private static final String INVALID_PARAM_MESSAGE = "Invalid parameter";
    private static final String NOT_FOUND_MESSAGE = "Recipe not found";
    private static final String UNSUPPORTED_METHOD_MESSAGE = "Unsupported method: ";

    private static final String PATH_SUCCESS = "$.success";
    private static final String PATH_MESSAGE = "$.message";
    private static final String PATH_RESULT = "$.result";
    private static final String PATH_RESULT_CONTENT = PATH_RESULT + ".content";
    private static final String PATH_RESULT_CONTENT_LENGTH = PATH_RESULT + ".content.length()";
    private static final String PATH_RESULT_FIRST_TITLE = PATH_RESULT + ".content[0].title";
    private static final String PATH_RESULT_PAGE_NUMBER = PATH_RESULT + ".pageNumber";
    private static final String PATH_RESULT_PAGE_SIZE = PATH_RESULT + ".pageSize";
    private static final String PATH_RESULT_TOTAL_ELEMENTS = PATH_RESULT + ".totalElements";
    private static final String PATH_RESULT_TOTAL_PAGES = PATH_RESULT + ".totalPages";
    private static final String PATH_RESULT_LAST = PATH_RESULT + ".last";

    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_DELETE = "DELETE";

    private static final String CREATE_RECIPE_URL = BASE_URL;
    private static final String UPDATE_RECIPE_URL = BASE_URL + "/" + VALID_UUID;
    private static final String GET_RECIPE_URL = BASE_URL + "/" + VALID_UUID;
    private static final String DELETE_RECIPE_URL = BASE_URL + "/" + VALID_UUID;
    private static final String UPDATE_RECIPE_INVALID_UUID_URL = BASE_URL + "/" + INVALID_UUID;
    private static final String GET_RECIPE_INVALID_UUID_URL = BASE_URL + "/" + INVALID_UUID;
    private static final String DELETE_RECIPE_INVALID_UUID_URL = BASE_URL + "/" + INVALID_UUID;
    private static final String SEARCH_RECIPE_URL = BASE_URL + "/search";

    private static final String RECIPE_TITLE = "Nilagang Saging";

    @BeforeEach
    void setUp() {
        recipeService = mock(RecipeService.class);
        translatorService = mock(TranslatorService.class);
        objectMapper = new ObjectMapper();

        RecipeController controller = new RecipeController(recipeService, translatorService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new com.api.recipe.common.exception.ApiResponseExceptionHandler(translatorService))
                .build();
    }

    private String buildJson(RecipeRequestDto dto) throws Exception {
        return objectMapper.writeValueAsString(dto);
    }

    /**
     * Success Request Test
     */
    private RecipeRequestDto getValidRequest() {
        RecipeRequestDto dto = new RecipeRequestDto();
        dto.setTitle(RECIPE_TITLE);
        dto.setDescription("Boiled saba bananas, a classic Filipino snack or breakfast item.");
        dto.setInstruction("""
                    1. Peel the saba bananas if desired (optional).
                    2. In a pot, bring enough water to a boil.
                    3. Add the saba bananas and cook for 10–15 minutes or until tender.
                    4. Drain and serve warm, optionally with sugar or butter.
                """.trim());
        dto.setVegetarian(true);
        dto.setServings(2);

        RecipeRequestDto.IngredientRequestDto ingredient = new RecipeRequestDto.IngredientRequestDto();
        ingredient.setName("4 pieces saba banana");

        dto.setIngredients(List.of(ingredient));
        return dto;
    }

    private RecipeViewDto buildRecipeViewDto() {
        RecipeViewDto viewDto = new RecipeViewDto();
        viewDto.setUuid(UUID.randomUUID());
        viewDto.setCreatedAt(OffsetDateTime.now());
        viewDto.setTitle(RECIPE_TITLE);
        viewDto.setDescription("Boiled saba bananas, a classic Filipino snack or breakfast item.");
        viewDto.setInstruction("""
        1. Peel the saba bananas if desired (optional).
        2. In a pot, bring enough water to a boil.
        3. Add the saba bananas and cook for 10–15 minutes or until tender.
        4. Drain and serve warm, optionally with sugar or butter.
    """.trim());
        viewDto.setVegetarian(true);
        viewDto.setServings(2);

        RecipeViewDto.IngredientViewDto ingredientDto = new RecipeViewDto.IngredientViewDto();
        ingredientDto.setName("4 pieces saba banana");
        viewDto.setIngredients(List.of(ingredientDto));

        return viewDto;
    }

    @Test
    void createRecipe_success() throws Exception {
        when(translatorService.process(eq(CREATE_RECIPE_SUCCESS_KEY), (Locale) any())).thenReturn(CREATE_RECIPE_SUCCESS_MESSAGE);

        mockMvc.perform(post(CREATE_RECIPE_URL)
                        .contentType(JSON)
                        .content(buildJson(getValidRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(PATH_SUCCESS).value(true))
                .andExpect(jsonPath(PATH_MESSAGE).value(CREATE_RECIPE_SUCCESS_MESSAGE));
    }

    @Test
    void updateRecipe_success() throws Exception {
        when(translatorService.process(eq(UPDATE_RECIPE_SUCCESS_KEY), (Locale) any())).thenReturn(UPDATE_RECIPE_SUCCESS_MESSAGE);

        mockMvc.perform(put(UPDATE_RECIPE_URL)
                        .contentType(JSON)
                        .content(buildJson(getValidRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PATH_SUCCESS).value(true))
                .andExpect(jsonPath(PATH_MESSAGE).value(UPDATE_RECIPE_SUCCESS_MESSAGE));
    }

    @Test
    void getRecipe_success() throws Exception {
        when(translatorService.process(eq(GET_RECIPE_SUCCESS_KEY), (Locale) any())).thenReturn(GET_RECIPE_SUCCESS_MESSAGE);
        when(recipeService.getRecipe(any())).thenReturn(new RecipeViewDto());

        mockMvc.perform(get(GET_RECIPE_URL)
                        .contentType(JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PATH_SUCCESS).value(true))
                .andExpect(jsonPath(PATH_MESSAGE).value(GET_RECIPE_SUCCESS_MESSAGE));
    }

    @Test
    void deleteRecipe_success() throws Exception {
        when(translatorService.process(eq(DELETE_RECIPE_SUCCESS_KEY), (Locale) any())).thenReturn(DELETE_RECIPE_SUCCESS_MESSAGE);

        mockMvc.perform(delete(DELETE_RECIPE_URL)
                        .contentType(JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PATH_SUCCESS).value(true))
                .andExpect(jsonPath(PATH_MESSAGE).value(DELETE_RECIPE_SUCCESS_MESSAGE));
    }

    @Test
    void searchRecipe_success() throws Exception {
        RecipeViewDto viewDto = buildRecipeViewDto();
        Page<RecipeViewDto> recipePage = new PageImpl<>(List.of(viewDto), PageRequest.of(0, 10), 1);

        when(recipeService.searchRecipes(any(), any(), any(), any(), any(), any()))
                .thenReturn(recipePage);
        when(translatorService.process(eq(SEARCH_RECIPE_SUCCESS_KEY), any(Locale.class)))
                .thenReturn(SEARCH_RECIPE_SUCCESS_MESSAGE);

        mockMvc.perform(get(SEARCH_RECIPE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PATH_SUCCESS).value(true))
                .andExpect(jsonPath(PATH_MESSAGE).value(SEARCH_RECIPE_SUCCESS_MESSAGE))
                .andExpect(jsonPath(PATH_SUCCESS).value(true))
                .andExpect(jsonPath(PATH_MESSAGE).value(SEARCH_RECIPE_SUCCESS_MESSAGE))
                .andExpect(jsonPath(PATH_RESULT_CONTENT).isArray())
                .andExpect(jsonPath(PATH_RESULT_CONTENT_LENGTH).value(1))
                .andExpect(jsonPath(PATH_RESULT_FIRST_TITLE).value(RECIPE_TITLE))
                .andExpect(jsonPath(PATH_RESULT_PAGE_NUMBER).value(0))
                .andExpect(jsonPath(PATH_RESULT_PAGE_SIZE).value(10))
                .andExpect(jsonPath(PATH_RESULT_TOTAL_ELEMENTS).value(1))
                .andExpect(jsonPath(PATH_RESULT_TOTAL_PAGES).value(1))
                .andExpect(jsonPath(PATH_RESULT_LAST).value(true));
    }

    @Test
    void searchRecipe_emptyList() throws Exception {
        Page<RecipeViewDto> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        when(recipeService.searchRecipes(any(), any(), any(), any(), any(), any()))
                .thenReturn(emptyPage);
        when(translatorService.process(eq(SEARCH_RECIPE_EMPTY_KEY), any(Locale.class)))
                .thenReturn(SEARCH_RECIPE_EMPTY_MESSAGE);

        mockMvc.perform(get(SEARCH_RECIPE_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(PATH_SUCCESS).value(true))
                .andExpect(jsonPath(PATH_MESSAGE).value(SEARCH_RECIPE_EMPTY_MESSAGE))
                .andExpect(jsonPath(PATH_RESULT_CONTENT).isArray())
                .andExpect(jsonPath(PATH_RESULT_CONTENT_LENGTH).value(0))
                .andExpect(jsonPath(PATH_RESULT_TOTAL_ELEMENTS).value(0))
                .andExpect(jsonPath(PATH_RESULT_TOTAL_PAGES).value(0))
                .andExpect(jsonPath(PATH_RESULT_PAGE_SIZE).value(10))
                .andExpect(jsonPath(PATH_RESULT_PAGE_NUMBER).value(0))
                .andExpect(jsonPath(PATH_RESULT_LAST).value(true));

    }

    /**
     * Request Validation Test
     */
    private void performRequestValidationTest(String method, String url, RecipeRequestDto dto) throws Exception {
        when(translatorService.process(VALIDATION_KEY)).thenReturn(RecipeControllerTest.VALIDATION_MESSAGE);

        MockHttpServletRequestBuilder requestBuilder = switch (method) {
            case METHOD_POST -> post(url);
            case METHOD_PUT -> put(url);
            default -> throw new IllegalArgumentException(UNSUPPORTED_METHOD_MESSAGE + method);
        };

        mockMvc.perform(requestBuilder
                        .contentType(JSON)
                        .content(buildJson(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(PATH_MESSAGE).value(RecipeControllerTest.VALIDATION_MESSAGE));
    }

    @Test
    void createRecipe_missingTitle_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setTitle(null);
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_missingTitle_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setTitle(null);
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    @Test
    void createRecipe_missingDescription_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setDescription(null);
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_missingDescription_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setDescription(null);
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    @Test
    void createRecipe_missingIngredientList_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setIngredients(null);
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_missingIngredientList_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setIngredients(null);
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    @Test
    void createRecipe_emptyIngredientList_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setIngredients(new ArrayList<>());
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_emptyIngredientList_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setIngredients(new ArrayList<>());
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    @Test
    void createRecipe_missingIngredientName_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.getIngredients().get(0).setName("");
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_missingIngredientName_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.getIngredients().get(0).setName("");
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    @Test
    void createRecipe_missingVegetarian_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setVegetarian(null);
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_missingVegetarian_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setVegetarian(null);
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    @Test
    void createRecipe_missingServings_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setServings(null);
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_missingServings_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setServings(null);
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    @Test
    void createRecipe_invalidServings_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setServings(0);
        performRequestValidationTest(METHOD_POST, CREATE_RECIPE_URL, dto);
    }

    @Test
    void updateRecipe_invalidServings_returnsValidationError() throws Exception {
        RecipeRequestDto dto = getValidRequest();
        dto.setServings(0);
        performRequestValidationTest(METHOD_PUT, UPDATE_RECIPE_URL, dto);
    }

    /**
     * Invalid Parameter Test
     */
    private void performInvalidParamTest(String method, String url) throws Exception {
        when(translatorService.process(eq(INVALID_PARAM_KEY), any(Object[].class)))
                .thenReturn(INVALID_PARAM_MESSAGE);

        MockHttpServletRequestBuilder requestBuilder = switch (method) {
            case METHOD_GET -> get(url);
            case METHOD_PUT -> put(url).contentType(JSON).content(buildJson(getValidRequest()));
            case METHOD_DELETE -> delete(url);
            default -> throw new IllegalArgumentException(UNSUPPORTED_METHOD_MESSAGE + method);
        };

        mockMvc.perform(requestBuilder.contentType(JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(PATH_SUCCESS).value(false))
                .andExpect(jsonPath(PATH_MESSAGE).value(INVALID_PARAM_MESSAGE));
    }

    @Test
    void getRecipe_invalidUuid_returnsInvalidParameterError() throws Exception {
        performInvalidParamTest(METHOD_GET, GET_RECIPE_INVALID_UUID_URL);
    }

    @Test
    void updateRecipe_invalidUuid_returnsInvalidParameterError() throws Exception {
        performInvalidParamTest(METHOD_PUT, UPDATE_RECIPE_INVALID_UUID_URL);
    }

    @Test
    void deleteRecipe_invalidUuid_returnsInvalidParameterError() throws Exception {
        performInvalidParamTest(METHOD_DELETE, DELETE_RECIPE_INVALID_UUID_URL);
    }

    /**
     * Not Found Test
     */
    private void performNotFoundTest(String method, String url) throws Exception {
        String errorMessage = NOT_FOUND_MESSAGE;

        switch (method) {
            case METHOD_GET -> doThrow(new jakarta.persistence.EntityNotFoundException(errorMessage))
                    .when(recipeService).getRecipe(any());
            case METHOD_PUT -> doThrow(new jakarta.persistence.EntityNotFoundException(errorMessage))
                    .when(recipeService).updateRecipe(any(), any());
            case METHOD_DELETE -> doThrow(new jakarta.persistence.EntityNotFoundException(errorMessage))
                    .when(recipeService).deleteRecipe(any());
            default -> throw new IllegalArgumentException(UNSUPPORTED_METHOD_MESSAGE + method);
        }

        MockHttpServletRequestBuilder requestBuilder = switch (method) {
            case METHOD_GET -> get(url);
            case METHOD_PUT -> put(url).contentType(JSON).content(buildJson(getValidRequest()));
            case METHOD_DELETE -> delete(url);
            default -> throw new IllegalArgumentException(UNSUPPORTED_METHOD_MESSAGE + method);
        };

        mockMvc.perform(requestBuilder.contentType(JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(PATH_SUCCESS).value(false))
                .andExpect(jsonPath(PATH_MESSAGE).value(errorMessage));
    }

    @Test
    void getRecipe_notFound_returnsNotFoundError() throws Exception {
        performNotFoundTest(METHOD_GET, GET_RECIPE_URL);
    }

    @Test
    void updateRecipe_notFound_returnsNotFoundError() throws Exception {
        performNotFoundTest(METHOD_PUT, UPDATE_RECIPE_URL);
    }

    @Test
    void deleteRecipe_notFound_returnsNotFoundError() throws Exception {
        performNotFoundTest(METHOD_DELETE, DELETE_RECIPE_URL);
    }

}
