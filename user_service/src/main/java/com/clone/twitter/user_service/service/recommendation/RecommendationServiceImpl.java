package com.clone.twitter.user_service.service.recommendation;

import com.clone.twitter.user_service.dto.recommendation.RecommendationDto;
import com.clone.twitter.user_service.dto.recommendation.SkillOfferDto;
import com.clone.twitter.user_service.entity.Skill;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.UserSkillGuarantee;
import com.clone.twitter.user_service.entity.recommendation.Recommendation;
import com.clone.twitter.user_service.mapper.recommendation.RecommendationMapper;
import com.clone.twitter.user_service.repository.SkillRepository;
import com.clone.twitter.user_service.repository.UserRepository;
import com.clone.twitter.user_service.repository.recommendation.RecommendationRepository;
import com.clone.twitter.user_service.repository.recommendation.SkillOfferRepository;
import com.clone.twitter.user_service.validator.Recommendation.RecommendationValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;
    private final SkillOfferRepository skillOfferRepository;
    private final SkillRepository skillRepository;
    private final RecommendationValidator validator;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RecommendationDto create(RecommendationDto recommendationDto) {
        User author = findUserByUserId(recommendationDto.getAuthorId());
        User receiver = findUserByUserId(recommendationDto.getReceiverId());

        validator.validateAuthorAndReceiver(recommendationDto, author, receiver);

        recommendationRepository.create(
                recommendationDto.getAuthorId(),
                recommendationDto.getReceiverId(),
                recommendationDto.getContent());

        processSkillsAndGuarantees(recommendationDto);

        return recommendationDto;
    }

    @Override
    @Transactional
    public RecommendationDto updateRecommendation(RecommendationDto updatedRecommendationDto) {

        User author = findUserByUserId(updatedRecommendationDto.getAuthorId());
        User receiver = findUserByUserId(updatedRecommendationDto.getReceiverId());

        validator.validateAuthorAndReceiver(updatedRecommendationDto, author, receiver);

        skillOfferRepository.deleteAllByRecommendationId(updatedRecommendationDto.getId());
        processSkillsAndGuarantees(updatedRecommendationDto);

        recommendationRepository.update(
                updatedRecommendationDto.getAuthorId(),
                updatedRecommendationDto.getReceiverId(),
                updatedRecommendationDto.getContent());

        return updatedRecommendationDto;
    }

    @Override
    public RecommendationDto delete(long id) {
        validator.checkIfRecommendationNotExist(id);

        Optional<Recommendation> recommendationToDelete = recommendationRepository.findById(id);

        if (recommendationToDelete.isEmpty()) {
            throw new EntityNotFoundException("Recommendation with id: " + id + " not found");
        }

        RecommendationDto recommendationDto = recommendationMapper.toDto(recommendationToDelete.get());

        recommendationRepository.deleteById(id);

        return recommendationDto;
    }

    @Override
    @Transactional
    public List<RecommendationDto> getAllUserRecommendations(long receiverId) {
        Page<Recommendation> recommendationPage = recommendationRepository.findAllByReceiverId(
                receiverId,
                PageRequest.of(0, Integer.MAX_VALUE));

        return recommendationPage.getContent()
                .stream()
                .map(recommendationMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<RecommendationDto> getAllGivenRecommendations(long authorId) {
        Page<Recommendation> recommendationPage = recommendationRepository
                .findAllByAuthorId(
                        authorId,
                        PageRequest.of(0, Integer.MAX_VALUE));

        return recommendationPage
                .stream()
                .map(recommendationMapper::toDto)
                .toList();
    }


    @Transactional
    public void processSkillsAndGuarantees(RecommendationDto recommendationDto) {
        User author = findUserByUserId(recommendationDto.getAuthorId());
        User receiver = findUserByUserId(recommendationDto.getReceiverId());

        List<Skill> existingReceiverSkills = skillRepository.findAllByUserId(receiver.getId());
        List<SkillOfferDto> skillOffersDto = recommendationDto.getSkillOffers();

        if (skillOffersDto == null) {
            skillOffersDto = new ArrayList<>();
        }

        updateSkillsGuarantees(
                skillOffersDto,
                existingReceiverSkills,
                author,
                receiver);

        saveNewSkills(
                author,
                receiver,
                skillOffersDto);

        saveSkillOffers(skillOffersDto);
    }


    @Transactional
    public User findUserByUserId(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with ID: " + userId + " does not exist"));
    }

    private void updateSkillsGuarantees(
            List<SkillOfferDto> skillOffersDto,
            List<Skill> receiverSkills,
            User author,
            User receiver) {

        List<Long> skillOfferIds = skillOffersDto.stream()
                .map(SkillOfferDto::getSkillId).toList();

        receiverSkills.stream()
                .filter(skill -> skillOfferIds.contains(skill.getId()))
                .forEach(matchedSkill -> {
                    if (!validator.checkIsGuarantor(matchedSkill, author.getId())) {
                        addGuarantor(matchedSkill, author, receiver);
                    }
                });

        skillOffersDto.removeIf(offer -> skillOfferIds.contains(offer.getSkillId()));
    }

    private void addGuarantor(Skill skill, User author, User receiver) {
        UserSkillGuarantee guarantee = UserSkillGuarantee.builder()
                .user(receiver)
                .skill(skill)
                .guarantor(author)
                .build();

        List<UserSkillGuarantee> guarantees = Optional.ofNullable(skill.getGuarantees())
                .orElseGet(ArrayList::new);

        guarantees.add(guarantee);
    }

    @Transactional
    public void saveNewSkills(User author, User receiver, List<SkillOfferDto> skillOffersDto) {
        List<Skill> newSkills = skillRepository.findAllById(
                skillOffersDto.stream()
                        .map(SkillOfferDto::getSkillId)
                        .collect(Collectors.toList()));

        newSkills.forEach(skill -> addGuarantor(skill, author,
                receiver));
        receiver.getSkills().addAll(newSkills);

        userRepository.save(receiver);
    }

    @Transactional
    public void saveSkillOffers(List<SkillOfferDto> skillOffersDto) {
        skillOffersDto.forEach(offer -> skillOfferRepository.create(
                offer.getSkillId(),
                offer.getRecommendationId()));
    }
}
