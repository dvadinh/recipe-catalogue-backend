package com.dvaults.recipecatalogue.modules.core.services.api.implementation;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.common.mappers.CommonMapper;
import com.dvaults.recipecatalogue.configs.AwsS3Configs;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import com.dvaults.recipecatalogue.modules.core.repositories.StepRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.BeneficiaryService;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.MediaService;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.StepService;
import com.dvaults.recipecatalogue.security.authentication.tokens.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StepServiceImpl implements StepService {

  private final MediaService mediaService;
  private final StepRepository stepRepository;
  private final CommonMapper commonMapper;

  @Override
  @Transactional
  public MediaResponse updateMediaByStep(
      Step step,
      MultipartFile media,
      UserPrincipal principal
  ) {

    String mediaS3Key = String.format(AwsS3Configs.STEP_KEY_NAME_TEMPLATE, step.getId());
    step.setMediaContentS3Key(mediaS3Key);
    step.setMediaContentName(media.getOriginalFilename());
    step.setMediaContentType(media.getContentType());
    stepRepository.save(step);

    mediaService.updateMedia(mediaS3Key, media);

    return commonMapper.toMediaResponse(
        step.getMediaContentS3Key(),
        step.getMediaContentName(),
        step.getMediaContentType()
    );

  }

  @Override
  @Transactional
  public void deleteMediaByStep(
      Step step,
      UserPrincipal principal
  ) {

    String s3KeyToBeDeleted = step.getMediaContentS3Key();
    step.setMediaContentS3Key(null);
    step.setMediaContentName(null);
    step.setMediaContentType(null);
    stepRepository.save(step);

    mediaService.deleteMedia(List.of(s3KeyToBeDeleted));

  }

}
