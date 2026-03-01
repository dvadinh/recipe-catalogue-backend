package com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.implementation;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.common.errors.exceptions.ResourceNotFoundException;
import com.dvaults.recipecatalogue.modules.core.errors.MediaErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.errors.SectionErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.repositories.SectionRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.SectionService;
import com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification.SectionAuthorizationProxyService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SectionAuthorizationProxyServiceImpl implements SectionAuthorizationProxyService {

  private final SectionService sectionService;
  private final SectionRepository sectionRepository;

  @Override
  public MediaResponse updateMediaById(
      UserPrincipal principal,
      long id,
      MultipartFile media
  ) {

    if (media == null || media.isEmpty()) {
      throw new RequestValidationException(MediaErrorDictionary.INVALID_MEDIA_DETAILS_001);
    }

    return sectionService.updateMediaBySection(
        sectionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(SectionErrorDictionary.SECTION_NOT_FOUND_001)),
        media,
        principal
    );
  }

  @Override
  public void deleteMediaById(
      UserPrincipal principal,
      long id
  ) {
    sectionService.deleteMediaBySection(
        sectionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(SectionErrorDictionary.SECTION_NOT_FOUND_001)),
        principal
    );
  }

}
