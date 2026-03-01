package com.dvaults.recipecatalogue.modules.core.services.authorizationproxy.specification;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

public interface SectionAuthorizationProxyService {

  MediaResponse updateMediaById(
      UserPrincipal principal,
      long id,
      MultipartFile media
  );

  void deleteMediaById(
      UserPrincipal principal,
      long id
  );

}
