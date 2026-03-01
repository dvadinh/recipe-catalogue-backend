package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface StepService {

  MediaResponse updateMediaByStep(
      Step step,
      MultipartFile media,
      UserPrincipal principal
  );

  void deleteMediaByStep(
      Step step,
      UserPrincipal principal
  );

}
