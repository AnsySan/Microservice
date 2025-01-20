package com.clone.twitter.project_service.validation.donation;

import com.clone.twitter.project_service.dto.donation.DonationCreateDto;
import com.clone.twitter.project_service.exceptions.DataValidationException;
import com.clone.twitter.project_service.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonationValidator {
    private final CampaignRepository campaignRepository;
    public void validateSendDonation(DonationCreateDto donationDto) {
        validateCampaign(donationDto);

    }

    private void validateCampaign(DonationCreateDto donationDto) {
        boolean isActive = campaignRepository.isCampaignActive(donationDto.getCampaignId());
        if(!isActive){
            throw new DataValidationException("Donation cannot be sent to inactive campaign");
        }
    }
}
