package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.PutRecipeContext;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PatchRecipeAccessLevelRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeDetailsResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeSummaryResponse;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {

  List<RecipeDetailsResponse> findAllByUserId(long userId);

  @PreAuthorize("@recipeAuthorizationService.preAuthorizeFindByRecipe(principal, #recipe)")
  RecipeDetailsResponse findByRecipe(Recipe recipe);

  RecipeDetailsResponse create(
      PostRecipeRequest screenedRequest,
      User principalUser
  );

  @PreAuthorize("@recipeAuthorizationService.preAuthorizeUpdateByRecipeAndPutRecipeItems(principal, #recipe)")
  RecipeDetailsResponse updateByRecipeAndPutRecipeItems(
      Recipe recipe,
      PutRecipeContext putRecipeContext,
      UserPrincipal principal
  );

  @PreAuthorize("@recipeAuthorizationService.preAuthorizeUpdateAccessLevelByRecipe(principal, #recipe)")
  RecipeSummaryResponse updateAccessLevelByRecipe(
      Recipe recipe,
      PatchRecipeAccessLevelRequest screenedRequest
  );

  @PreAuthorize("@recipeAuthorizationService.preAuthorizeDeleteAllByRecipes(principal, #recipes)")
  void deleteAllByRecipes(
      List<Recipe> recipes,
      UserPrincipal principal
  );

  @PreAuthorize("@recipeAuthorizationService.preAuthorizeUpdateMediaByRecipe(principal, #recipe)")
  MediaResponse updateMediaByRecipe(
      Recipe recipe,
      MultipartFile media,
      UserPrincipal principal
  );

  @PreAuthorize("@recipeAuthorizationService.preAuthorizeDeleteMediaByRecipe(principal, #recipe)")
  void deleteMediaByRecipe(
      Recipe recipe,
      UserPrincipal principal
  );

}
