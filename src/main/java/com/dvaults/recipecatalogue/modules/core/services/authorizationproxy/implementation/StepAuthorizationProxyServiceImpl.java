package com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.modules.core.errors.MediaErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.StepErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.repositories.StepRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.StepService;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.SectionAuthorizationProxyService;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.StepAuthorizationProxyService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StepAuthorizationProxyServiceImpl implements StepAuthorizationProxyService {

  private final StepService stepService;
  private final StepRepository stepRepository;

  @Override
  public MediaResponse updateMediaById(
      UserPrincipal principal,
      long id,
      MultipartFile media
  ) {

    if (media == null || media.isEmpty()) {
      throw new RequestValidationException(MediaErrorDictionary.INVALID_MEDIA_DETAILS_001);
    }

    return stepService.updateMediaByStep(
        stepRepository.findByIdFetchSectionRecipe(id)
            .orElseThrow(() -> new ResourceNotFoundException(StepErrorDictionary.STEP_NOT_FOUND_001)),
        media,
        principal
    );

  }

  @Override
  public void deleteMediaById(
      UserPrincipal principal,
      long id
  ) {
    stepService.deleteMediaByStep(
        stepRepository.findByIdFetchSectionRecipe(id)
            .orElseThrow(() -> new ResourceNotFoundException(StepErrorDictionary.STEP_NOT_FOUND_001)),
        principal
    );
  }

}
