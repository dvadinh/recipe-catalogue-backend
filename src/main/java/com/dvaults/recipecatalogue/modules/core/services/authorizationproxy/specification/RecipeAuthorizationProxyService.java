package com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.DeleteRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PatchRecipeAccessLevelRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PostRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.requests.PutRecipeRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeDetailsResponse;
import com.dvaults.recipecatalogue.modules.core.dtos.recipe.responses.RecipeSummaryResponse;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface RecipeAuthorizationProxyService {

  List<RecipeDetailsResponse> findAll(UserPrincipal principal);

  RecipeDetailsResponse findById(
      UserPrincipal principal,
      long id
  );

  RecipeDetailsResponse create(
      UserPrincipal principal,
      PostRecipeRequest postRecipeRequest
  );

  RecipeDetailsResponse updateById(
      UserPrincipal principal,
      long id,
      PutRecipeRequest putRecipeRequest
  );

  RecipeSummaryResponse updateAccessLevelById(
      UserPrincipal principal,
      long id,
      PatchRecipeAccessLevelRequest patchRecipeAccessLevelRequest
  );

  MediaResponse updateMediaById(
      UserPrincipal principal,
      long id,
      MultipartFile media
  );

  void deleteAll(
      UserPrincipal principal,
      DeleteRecipeRequest deleteRecipeRequest
  );

  void deleteMediaById(
      UserPrincipal principal,
      long id
  );

}
