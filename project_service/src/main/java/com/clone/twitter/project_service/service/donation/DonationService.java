package com.clone.twitter.project_service.service.donation;

import com.clone.twitter.project_service.dto.donation.DonationCreateDto;
import com.clone.twitter.project_service.dto.donation.DonationDto;
import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;

import java.util.List;

public interface DonationService {
    DonationDto sendDonation(long userId, DonationCreateDto donationDto);

    DonationDto getDonationById(long donationId);

    List<DonationDto> getAllDonationsByUserId(long userId, DonationFilterDto filter);
}
