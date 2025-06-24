package com.api.recipe.main.specification;

import com.api.recipe.common.entity.BaseEntity;
import com.api.recipe.common.util.ConstantUtil;
import com.api.recipe.main.entity.Ingredient;
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
    public static Specification<Recipe> containsInstruction(String instruction) {
        return (root, query, cb) ->
                StringUtils.hasText(instruction)
                        ? cb.like(cb.lower(root.get(Recipe.Fields.INSTRUCTION)), like(instruction))
                        : cb.conjunction();
    }

    /**
     * WHERE LOWER(ingredient.name) LIKE %:includedIngredient%
     */
    public static Specification<Recipe> includeIngredients(List<String> ingredients) {
        return (root, query, cb) -> {
            if (ingredients == null || ingredients.isEmpty()) {
                return cb.conjunction();
            }

            Join<Recipe, ?> ingredientJoin = root.join(Recipe.Fields.INGREDIENTS);

            List<jakarta.persistence.criteria.Predicate> predicates = ingredients.stream()
                    .filter(StringUtils::hasText)
                    .map(ingredient ->
                            cb.like(cb.lower(ingredientJoin.get(Ingredient.Fields.NAME)), like(ingredient)))
                    .toList();

            return cb.or(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }


    /**
     * WHERE id NOT IN (
     * SELECT recipe_id FROM ingredient
     * WHERE LOWER(name) LIKE %:excludedIngredient%
     * )
     */
    public static Specification<Recipe> excludeIngredients(List<String> ingredients) {
        return (root, query, cb) -> {
            if (ingredients == null || ingredients.isEmpty()) {
                return cb.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Recipe> subRoot = subquery.from(Recipe.class);
            Join<Recipe, ?> subIngredientJoin = subRoot.join(Recipe.Fields.INGREDIENTS);

            List<jakarta.persistence.criteria.Predicate> predicates = ingredients.stream()
                    .filter(StringUtils::hasText)
                    .map(ingredient ->
                            cb.like(cb.lower(subIngredientJoin.get(Ingredient.Fields.NAME)), like(ingredient)))
                    .toList();

            subquery.select(subRoot.get(BaseEntity.Fields.ID))
                    .where(cb.or(predicates.toArray(new jakarta.persistence.criteria.Predicate[0])));

            return cb.not(root.get(BaseEntity.Fields.ID).in(subquery));
        };
    }

    private static String like(String keyword) {
        return ConstantUtil.WILDCARD_DELIMITER + keyword.toLowerCase() + ConstantUtil.WILDCARD_DELIMITER;
    }
}
