package com.dvaults.recipecatalogue.modules.core.authorization;

import com.dvaults.recipecatalogue.modules.auth.models.Authority;
import com.dvaults.recipecatalogue.modules.core.errors.RecipeErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.modules.core.models.RecipeAccessLevel;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import com.dvaults.recipecatalogue.security.authorization.errors.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("recipeAuthorizationService")
public class RecipeAuthorizationService {

  public boolean preAuthorizeFindByRecipe(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (recipe.getAccessLevel() == RecipeAccessLevel.PRIVATE) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    if (recipe.getBeneficiaries()
        .stream()
        .anyMatch(beneficiary -> beneficiary.getUser().getId().equals(principal.getId()))
    ) {
      return true;
    }

    throw new AuthorizationException(RecipeErrorDictionary.RECIPE_ACCESS_DENIED_001);

  }

  public boolean preAuthorizeUpdateByRecipeAndPutRecipeItems(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(RecipeErrorDictionary.RECIPE_ACCESS_DENIED_002);

  }

  public boolean preAuthorizeUpdateAccessLevelByRecipe(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(RecipeErrorDictionary.RECIPE_ACCESS_DENIED_002);

  }

  public boolean preAuthorizeDeleteAllByRecipes(
      UserPrincipal principal,
      List<Recipe> recipes
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (recipes.stream()
        .allMatch(recipe -> principal.getId().equals(recipe.getOwner().getId()))
    ) {
      return true;
    }

    throw new AuthorizationException(RecipeErrorDictionary.RECIPE_ACCESS_DENIED_003);

  }

  public boolean preAuthorizeUpdateMediaByRecipe(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(RecipeErrorDictionary.RECIPE_ACCESS_DENIED_002);

  }

  public boolean preAuthorizeDeleteMediaByRecipe(
      UserPrincipal principal,
      Recipe recipe
  ) {

    if (principal.getAuthority() == Authority.ADMIN) {
      return true;
    }

    if (principal.getId().equals(recipe.getOwner().getId())) {
      return true;
    }

    throw new AuthorizationException(RecipeErrorDictionary.RECIPE_ACCESS_DENIED_003);

  }

}
