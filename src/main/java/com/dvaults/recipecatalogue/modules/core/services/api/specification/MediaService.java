package com.dvaults.recipecatalogue.modules.core.services.api.specification;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

  void updateMedia(
      String mediaS3Key,
      MultipartFile media
  );

  void deleteMedia(List<String> mediaS3Keys);

}
