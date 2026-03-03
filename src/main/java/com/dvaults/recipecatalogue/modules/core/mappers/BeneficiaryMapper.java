package com.dvaults.recipecatalogue.modules.core.mappers;

import com.dvaults.recipecatalogue.modules.auth.mappers.UserMapper;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.PostBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses.BeneficiaryResponse;
import com.dvaults.recipecatalogue.modules.core.models.Beneficiary;
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
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    uses = {
        UserMapper.class,
        RecipeMapper.class
    }
)
public abstract class BeneficiaryMapper {

  @Named("toBeneficiaryResponse")
  @Mapping(target = "user", source = "beneficiary.user", qualifiedByName = "toUserSummaryResponse")
  @Mapping(target = "recipe", source = "beneficiary.recipe", qualifiedByName = "toRecipeSummaryResponse")
  public abstract BeneficiaryResponse toBeneficiaryResponse(Beneficiary beneficiary);

  @Named("toBeneficiaryResponseList")
  @IterableMapping(qualifiedByName = "toBeneficiaryResponse")
  public abstract List<BeneficiaryResponse> toBeneficiaryResponseList(List<Beneficiary> beneficiaryList);

  @Named("screenPostBeneficiaryRequest")
  public abstract PostBeneficiaryRequest screenPostBeneficiaryRequest(PostBeneficiaryRequest postBeneficiaryRequest);

  @Named("screenDeleteBeneficiaryRequest")
  public abstract DeleteBeneficiaryRequest screenDeleteBeneficiaryRequest(DeleteBeneficiaryRequest deleteBeneficiaryRequest);

}
