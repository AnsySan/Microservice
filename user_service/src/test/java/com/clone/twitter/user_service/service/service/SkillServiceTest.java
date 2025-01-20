package com.clone.twitter.user_service.service.service;

import com.clone.twitter.user_service.dto.skill.SkillDto;
import com.clone.twitter.user_service.entity.Skill;
import com.clone.twitter.user_service.entity.User;
import com.clone.twitter.user_service.entity.recommendation.Recommendation;
import com.clone.twitter.user_service.entity.recommendation.SkillOffer;
import com.clone.twitter.user_service.exception.DataValidationException;
import com.clone.twitter.user_service.mapper.SkillMapper;
import com.clone.twitter.user_service.repository.SkillRepository;
import com.clone.twitter.user_service.repository.UserSkillGuaranteeRepository;
import com.clone.twitter.user_service.repository.recommendation.SkillOfferRepository;
import com.clone.twitter.user_service.service.SkillService;
import com.clone.twitter.user_service.validator.SkillValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {
    @InjectMocks
    private SkillService skillService;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private SkillOfferRepository skillOfferRepository;
    @Mock
    private Skill skill;
    @Mock
    private SkillOffer skillOffer;
    @Mock
    private Recommendation recommendation;
    @Mock
    private SkillValidator skillValidator;
    @Mock
    private User user;
    @Mock
    private UserSkillGuaranteeRepository skillGuaranteeRepository;
    @Spy
    private SkillMapper skillMapper;
    SkillDto skillDto;
    long userId = 1L;
    long skillId = 1L;
    @BeforeEach
    public void init() {
        skillDto = new SkillDto();
        skillDto.setId(1L);
        skillDto.setTitle("Driving a car");
    }

    @Test
    public void testSkillSave() throws DataValidationException {
        skillService.create(skillDto);
        Mockito.verify(skillRepository, Mockito.times(1)).save(skillMapper.toEntity(skillDto));
    }

    @Test
    public void testGetSkillsUser() {
        skillService.getUserSkills(userId);
        Mockito.verify(skillRepository, Mockito.times(1)).findAllByUserId(userId);
    }

    @Test
    public void testGetOfferedSkill() {
        skillService.getOfferedSkills(userId);
        Mockito.verify(skillRepository, Mockito.times(1)).findSkillsOfferedToUser(userId);
    }

    @Test
    public void testAssignSkillToUser() {
        List<SkillOffer> skillOffers = List.of(skillOffer, skillOffer, skillOffer, skillOffer);

        Mockito.when(skillValidator.validateSkill(skillId, userId)).thenReturn(true);
        Mockito.when(skillOfferRepository.findAllOffersOfSkill(skillId, userId)).
                thenReturn(skillOffers);
        Mockito.when(skillOffer.getRecommendation()).thenReturn(recommendation);
        Mockito.when(skillOffer.getRecommendation().getAuthor()).thenReturn(user);
        Mockito.when(skillOffer.getRecommendation().getAuthor().getId()).thenReturn(2L);

        skillService.acquireSkillFromOffers(skillId, userId);
        Mockito.verify(skillRepository, Mockito.times(1)).assignSkillToUser(skillId, userId);
    }
}