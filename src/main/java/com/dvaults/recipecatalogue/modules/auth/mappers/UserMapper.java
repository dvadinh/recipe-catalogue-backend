package com.dvaults.recipecatalogue.modules.auth.mappers;

import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PatchUsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.PostUserRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.requests.UsernamePasswordRequest;
import com.dvaults.recipecatalogue.modules.auth.dtos.user.responses.UserResponse;
import com.dvaults.recipecatalogue.modules.auth.models.User;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public abstract class UserMapper {

  @Named("toUserResponse")
  public abstract UserResponse toUserResponse(User user);

  @Named("toUserResponseList")
  @IterableMapping(qualifiedByName = "toUserResponse")
  public abstract List<UserResponse> toUserResponseList(List<User> userList);

  @Named("screenPostUserRequest")
  @Mapping(
      target = "username",
      expression = "java(postUserRequest.username() == null ? null : postUserRequest.username().trim())"
  )
  @Mapping(
      target = "password",
      expression = "java(postUserRequest.password() == null ? null : postUserRequest.password().trim())"
  )
  @Mapping(
      target = "displayName",
      expression = "java(postUserRequest.displayName() == null ? null : postUserRequest.displayName().trim())"
  )
  @Mapping(
      target = "description",
      expression = "java(postUserRequest.description() == null ? null : postUserRequest.description().trim())"
  )
  public abstract PostUserRequest screenPostUserRequest(PostUserRequest postUserRequest);

  @Named("screenPatchUsernamePasswordRequest")
  @Mapping(
      target = "username",
      expression = "java(patchUsernamePasswordRequest.username() == null ? null : patchUsernamePasswordRequest.username().trim())"
  )
  @Mapping(
      target = "password",
      expression = "java(patchUsernamePasswordRequest.password() == null ? null : patchUsernamePasswordRequest.password().trim())"
  )
  public abstract PatchUsernamePasswordRequest screenPatchUsernamePasswordRequest(PatchUsernamePasswordRequest patchUsernamePasswordRequest);

  @Named("screenPatchUserRequest")
  @Mapping(
      target = "displayName",
      expression = "java(patchUserRequest.displayName() == null ? null : patchUserRequest.displayName().trim())"
  )
  @Mapping(
      target = "description",
      expression = "java(patchUserRequest.description() == null ? null : patchUserRequest.description().trim())"
  )
  public abstract PatchUserRequest screenPatchUserRequest(PatchUserRequest patchUserRequest);

  @Named("screenUsernamePasswordRequest")
  @Mapping(
      target = "username",
      expression = "java(usernamePasswordRequest.username() == null ? null : usernamePasswordRequest.username().trim())"
  )
  @Mapping(
      target = "password",
      expression = "java(usernamePasswordRequest.password() == null ? null : usernamePasswordRequest.password().trim())"
  )
  public abstract UsernamePasswordRequest screenUsernamePasswordRequest(UsernamePasswordRequest usernamePasswordRequest);

}
