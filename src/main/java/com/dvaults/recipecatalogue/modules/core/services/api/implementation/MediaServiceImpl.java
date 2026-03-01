package com.dvaults.recipecatalogue.modules.core.services.api.implementation;

import com.dvaults.recipecatalogue.common.errors.exceptions.BadGatewayException;
import com.dvaults.recipecatalogue.common.errors.exceptions.RequestValidationException;
import com.dvaults.recipecatalogue.modules.core.errors.MediaErrorDictionary;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {

  private final S3Client s3Client;

  @Value("${aws.s3.bucket-name}")
  private String bucketName;

  @Override
  public void updateMedia(
      String mediaS3Key,
      MultipartFile media
  ) {

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(mediaS3Key)
        .build();

    try {
      s3Client.putObject(
          putObjectRequest,
          RequestBody.fromBytes(media.getBytes())
      );
    } catch (S3Exception s3Exception) {
      log.error("Error uploading media content to S3: {}", s3Exception.getMessage());
      throw new BadGatewayException(MediaErrorDictionary.INVALID_MEDIA_DETAILS_003);
    } catch (IOException ioException) {
      log.error("Error reading media content from request: {}", ioException.getMessage());
      throw new RequestValidationException(MediaErrorDictionary.INVALID_MEDIA_DETAILS_002);
    }

  }

  @Override
  public void deleteMedia(List<String> mediaS3Keys) {

    List<String> validMediaS3Keys = mediaS3Keys.stream()
        .filter(StringUtils::hasText)
        .toList();

    if (validMediaS3Keys.isEmpty()) {
      return;
    }

    try {
      s3Client.deleteObjects(
          DeleteObjectsRequest.builder()
              .bucket(bucketName)
              .delete(Delete.builder()
                  .objects(validMediaS3Keys.stream()
                      .map(validMediaS3Key -> ObjectIdentifier.builder()
                          .key(validMediaS3Key)
                          .build())
                      .toList())
                  .build())
              .build()
      );
    } catch (S3Exception s3Exception) {
      log.error("Error deleting media content from S3: {}", s3Exception.getMessage());
    }
  }

}
