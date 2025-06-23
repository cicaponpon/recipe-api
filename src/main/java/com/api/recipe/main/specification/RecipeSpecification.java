package com.api.recipe.main.specification;

import com.api.recipe.common.entity.BaseEntity;
import com.api.recipe.main.entity.Recipe;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;

public class RecipeSpecification {

    private RecipeSpecification() {
    }

    /**
     * WHERE vegetarian = :vegetarian
     */
    public static Specification<Recipe> isVegetarian(Boolean vegetarian) {
        return (root, query, cb) ->
                vegetarian == null
                        ? cb.conjunction()
                        : cb.equal(root.get(Recipe.Fields.VEGETARIAN), vegetarian);
    }

    /**
     * WHERE servings = :servings
     */
    public static Specification<Recipe> hasServings(Integer servings) {
        return (root, query, cb) ->
                servings == null
                        ? cb.conjunction()
                        : cb.equal(root.get(Recipe.Fields.SERVINGS), servings);
    }

    /**
     * WHERE LOWER(instruction) LIKE %:keyword%
     */
    public static Specification<Recipe> containsInstruction(String keyword) {
        return (root, query, cb) ->
                StringUtils.hasText(keyword)
                        ? cb.like(cb.lower(root.get(Recipe.Fields.INSTRUCTION)), "%" + keyword.toLowerCase() + "%")
                        : cb.conjunction();
    }

    /**
     * WHERE ingredients IN (:includedIngredients)
     */
    public static Specification<Recipe> includeIngredients(List<String> ingredients) {
        return (root, query, cb) -> {
            if (ingredients == null || ingredients.isEmpty()) {
                return cb.conjunction();
            }
            Join<Recipe, String> ingredientJoin = root.join(Recipe.Fields.INGREDIENTS);
            return ingredientJoin.in(ingredients);
        };
    }

    /**
     * WHERE id NOT IN (
     * SELECT recipe_id FROM recipe_ingredients
     * WHERE ingredient IN (:excludedIngredients)
     * )
     */
    public static Specification<Recipe> excludeIngredients(List<String> ingredients) {
        return (root, query, cb) -> {
            if (ingredients == null || ingredients.isEmpty()) {
                return cb.conjunction();
            }

            Subquery<Long> excludedRecipeIdSubquery = query.subquery(Long.class);
            Root<Recipe> subRecipeRoot = excludedRecipeIdSubquery.from(Recipe.class);
            Join<Recipe, String> subIngredientJoin = subRecipeRoot.join(Recipe.Fields.INGREDIENTS);

            excludedRecipeIdSubquery.select(subRecipeRoot.get(BaseEntity.Fields.ID))
                    .where(subIngredientJoin.in(ingredients));

            return cb.not(root.get(BaseEntity.Fields.ID).in(excludedRecipeIdSubquery));
        };
    }
}
