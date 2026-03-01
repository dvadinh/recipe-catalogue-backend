package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.PutRecipeContext;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeResponse;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {

  List<RecipeResponse> findAllByUserId(long userId);

  RecipeResponse findByRecipe(
      Recipe recipe,
      UserPrincipal principal
  );

  RecipeResponse create(
      PostRecipeRequest screenedRequest,
      User principalUser
  );

  RecipeResponse updateByRecipeAndPutRecipeItems(
      Recipe recipe,
      PutRecipeContext putRecipeContext,
      UserPrincipal principal
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
