package com.clone.twitter.project_service.service.donation;

import com.clone.twitter.project_service.client.PaymentServiceClient;
import com.clone.twitter.project_service.dto.client.Currency;
import com.clone.twitter.project_service.dto.donation.DonationCreateDto;
import com.clone.twitter.project_service.dto.donation.DonationDto;
import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.exceptions.NotFoundException;
import com.clone.twitter.project_service.mapper.donation.DonationMapper;
import com.clone.twitter.project_service.model.Campaign;
import com.clone.twitter.project_service.model.Donation;
import com.clone.twitter.project_service.repository.CampaignRepository;
import com.clone.twitter.project_service.repository.DonationRepository;
import com.clone.twitter.project_service.service.donation.filter.DonationFilterService;
import com.clone.twitter.project_service.validation.donation.DonationValidator;
import com.clone.twitter.project_service.validation.user.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationServiceImplTest {

    @Mock
    private DonationMapper donationMapper;

    @Mock
    private DonationValidator donationValidator;

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private UserValidator userValidator;

    @Mock
    private DonationFilterService donationFilterService;

    @InjectMocks
    private DonationServiceImpl donationService;

    private DonationCreateDto donationCreateDto;
    private Donation donation;
    private Campaign campaign;
    private DonationDto donationDto;

    @BeforeEach
    void setUp() {
        donationCreateDto = DonationCreateDto.builder()
                .paymentNumber(123456L)
                .amount(new BigDecimal("100"))
                .campaignId(1L)
                .currency("USD")
                .build();

        campaign = Campaign.builder()
                .id(1L)
                .title("Test Campaign")
                .build();

        donation = Donation.builder()
                .id(1L)
                .paymentNumber(123456L)
                .amount(new BigDecimal("100"))
                .campaign(campaign)
                .currency(Currency.USD)
                .userId(1L)
                .build();

        donationDto = new DonationDto();
        donationDto.setPaymentNumber(123456L);
        donationDto.setAmount(new BigDecimal("100"));
        donationDto.setDonationTime(LocalDateTime.now());
        donationDto.setCampaignId(1L);
        donationDto.setCurrency("USD");
        donationDto.setUserId(1L);
    }

    @Test
    void sendDonation_shouldSendDonation() {
        when(campaignRepository.findById(anyLong())).thenReturn(Optional.of(campaign));
        when(donationMapper.toEntity(any(DonationCreateDto.class))).thenReturn(donation);
        when(donationRepository.save(any(Donation.class))).thenReturn(donation);
        when(donationMapper.toDto(any(Donation.class))).thenReturn(donationDto);

        DonationDto result = donationService.sendDonation(1L, donationCreateDto);

        assertNotNull(result);
        assertEquals(123456L, result.getPaymentNumber());

        verify(userValidator).validateUserExistence(1L);
        verify(donationValidator).validateSendDonation(donationCreateDto);
        verify(donationRepository).save(donation);
    }

    @Test
    void sendDonation_shouldThrowNotFoundException_whenCampaignNotFound() {
        when(campaignRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> donationService.sendDonation(1L, donationCreateDto));

        assertEquals("Campaign with id 1 not found", exception.getMessage());
    }

    @Test
    void getDonationById_shouldReturnDonation() {
        when(donationRepository.findById(anyLong())).thenReturn(Optional.of(donation));
        when(donationMapper.toDto(any(Donation.class))).thenReturn(donationDto);

        DonationDto result = donationService.getDonationById(1L);

        assertNotNull(result);
        assertEquals(123456L, result.getPaymentNumber());
    }

    @Test
    void getDonationById_shouldThrowNotFoundException_whenDonationNotFound() {
        when(donationRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> donationService.getDonationById(1L));

        assertEquals("Donation with id 1 not found", exception.getMessage());
    }

    @Test
    void getAllDonationsByUserId_shouldReturnDonationList() {
        when(donationRepository.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(donation));
        when(donationFilterService.applyFilters(any(), any(DonationFilterDto.class))).thenReturn(Stream.of(donation));
        when(donationMapper.toDto(any(Donation.class))).thenReturn(donationDto);

        List<DonationDto> result = donationService.getAllDonationsByUserId(1L, new DonationFilterDto());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(123456L, result.get(0).getPaymentNumber());
    }
}
