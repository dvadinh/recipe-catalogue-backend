package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface StepService {

  @PreAuthorize("@stepAuthorizationService.preAuthorizeUpdateMediaByStep(principal, #step)")
  MediaResponse updateMediaByStep(
      Step step,
      MultipartFile media,
      UserPrincipal principal
  );

  @PreAuthorize("@stepAuthorizationService.preAuthorizeDeleteMediaByStep(principal, #step)")
  void deleteMediaByStep(
      Step step,
      UserPrincipal principal
  );

}
