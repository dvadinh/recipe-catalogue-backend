package com.dvaults.recipecatalogue.common.mappers;

import com.dvaults.recipecatalogue.common.dtos.responses.MediaResponse;
import com.dvaults.recipecatalogue.configs.AwsS3Configs;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.modules.core.models.Section;
import com.dvaults.recipecatalogue.modules.core.models.Step;
import lombok.AccessLevel;
import lombok.Setter;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
@Setter(value = AccessLevel.PROTECTED, onMethod_ = @Autowired)
public abstract class CommonMapper {

  private S3Presigner s3Presigner;

  @Setter(AccessLevel.NONE)
  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  @Named("toMediaResponse")
  public MediaResponse toMediaResponse(
      String keyName,
      String mediaName,
      String mediaContentType
  ) {

    if (keyName == null) {
      return null;
    }

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .getObjectRequest(getObjectRequest)
        .signatureDuration(Duration.ofSeconds(AwsS3Configs.PRESIGNED_URL_EXPIRATION_TIME))
        .build();

    PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

    return new MediaResponse(
        presignedGetObjectRequest.url().toExternalForm(),
        mediaName,
        mediaContentType
    );

  }

}
