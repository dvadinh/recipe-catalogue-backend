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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {

  List<RecipeDetailsResponse> findAllByUserId(long userId);

  RecipeDetailsResponse findByRecipe(
      Recipe recipe,
      UserPrincipal principal
  );

  RecipeDetailsResponse create(
      PostRecipeRequest screenedRequest,
      User principalUser
  );

  RecipeDetailsResponse updateByRecipeAndPutRecipeItems(
      Recipe recipe,
      PutRecipeContext putRecipeContext,
      UserPrincipal principal
  );

  RecipeSummaryResponse updateAccessLevelByRecipe(
      Recipe recipe,
      PatchRecipeAccessLevelRequest screenedRequest
  );

  void deleteAllByRecipes(
      List<Recipe> recipes,
      UserPrincipal principal
  );

  MediaResponse updateMediaByRecipe(
      Recipe recipe,
      MultipartFile media,
      UserPrincipal principal
  );

  void deleteMediaByRecipe(
      Recipe recipe,
      UserPrincipal principal
  );

}
