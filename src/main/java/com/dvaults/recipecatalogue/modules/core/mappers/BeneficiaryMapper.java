package com.dvaults.recipecatalogue.modules.core.mappers;

import com.dvaults.recipecatalogue.modules.auth.mappers.LinkedOAuth2AccountMapper;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    builder = @Builder(disableBuilder = true),
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
    uses = {
        LinkedOAuth2AccountMapper.class
    }
)
public abstract class BeneficiaryMapper {
}
