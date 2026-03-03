package com.dvaults.recipecatalogue.modules.core.services.api.implementation;

import com.dvaults.recipecatalogue.modules.auth.models.User;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.requests.DeleteBeneficiaryRequest;
import com.dvaults.recipecatalogue.modules.core.dtos.beneficiary.responses.BeneficiaryResponse;
import com.dvaults.recipecatalogue.modules.core.mappers.BeneficiaryMapper;
import com.dvaults.recipecatalogue.modules.core.models.Beneficiary;
import com.dvaults.recipecatalogue.modules.core.models.Recipe;
import com.dvaults.recipecatalogue.modules.core.repositories.BeneficiaryRepository;
import com.dvaults.recipecatalogue.modules.core.services.api.specification.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

  private final BeneficiaryRepository beneficiaryRepository;

  private final BeneficiaryMapper beneficiaryMapper;

  @Override
  public List<BeneficiaryResponse> findAllByRecipe(Recipe recipe) {
    return beneficiaryMapper.toBeneficiaryResponseList(
        recipe.getBeneficiaries()
            .stream()
            .toList()
    );
  }

  @Override
  @Transactional
  public List<BeneficiaryResponse> createByRecipeAndUsers(
      Recipe recipe,
      List<User> users
  ) {

    Set<Long> existingBeneficiaryUserIds = recipe.getBeneficiaries()
        .stream()
        .map(beneficiary -> beneficiary.getUser().getId())
        .collect(Collectors.toSet());

    List<Beneficiary> beneficiaries = new ArrayList<>();
    users.forEach(user -> {
      if (!existingBeneficiaryUserIds.contains(user.getId())) {
        beneficiaries.add(
            Beneficiary.builder()
                .recipe(recipe)
                .user(user)
                .build()
        );
      }
    });

    return beneficiaryMapper.toBeneficiaryResponseList(
        beneficiaryRepository.saveAll(beneficiaries)
    );

  }

  @Override
  @Transactional
  public void deleteAllByRecipe(
      Recipe recipe,
      DeleteBeneficiaryRequest screenedRequest
  ) {
    beneficiaryRepository.deleteAll(
        recipe.getBeneficiaries()
            .stream()
            .filter(beneficiary -> screenedRequest.userIds().contains(beneficiary.getUser().getId()))
            .toList()
    );
  }

}
