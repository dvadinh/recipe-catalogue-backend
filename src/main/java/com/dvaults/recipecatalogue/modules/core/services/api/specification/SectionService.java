package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface SectionService {

  MediaResponse updateMediaBySection(
      Section section,
      MultipartFile media,
      UserPrincipal principal
  );

  void deleteMediaBySection(
      Section section,
      UserPrincipal principal
  );

}
