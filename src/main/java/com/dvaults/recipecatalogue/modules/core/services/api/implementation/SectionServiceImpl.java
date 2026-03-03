package com.dvaults.recipecatalogue.modules.core.services.api.implementation;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.mappers.CommonMapper;
import com.dvaults.recipecatalogue.configs.AwsS3Configs;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.modules.core.repositories.SectionRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.BeneficiaryService;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.MediaService;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.SectionService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {

  private final MediaService mediaService;

  private final SectionRepository sectionRepository;

  private final CommonMapper commonMapper;

  @Override
  @Transactional
  public MediaResponse updateMediaBySection(
      Section section,
      MultipartFile media,
      UserPrincipal principal
  ) {

    String mediaS3Key = String.format(AwsS3Configs.SECTION_KEY_NAME_TEMPLATE, section.getId());
    section.setMediaContentS3Key(mediaS3Key);
    section.setMediaContentName(media.getOriginalFilename());
    section.setMediaContentType(media.getContentType());
    sectionRepository.save(section);

    mediaService.updateMedia(mediaS3Key, media);

    return commonMapper.toMediaResponse(
        section.getMediaContentS3Key(),
        section.getMediaContentName(),
        section.getMediaContentType()
    );

  }

  @Override
  @Transactional
  public void deleteMediaBySection(
      Section section,
      UserPrincipal principal
  ) {

    String s3KeyToBeDeleted = section.getMediaContentS3Key();
    section.setMediaContentS3Key(null);
    section.setMediaContentName(null);
    section.setMediaContentType(null);
    sectionRepository.save(section);

    mediaService.deleteMedia(List.of(s3KeyToBeDeleted));

  }

}
