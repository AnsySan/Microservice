package com.clone.twitter.project_service.mapper.donation;

import com.clone.twitter.project_service.dto.client.PaymentRequest;
import com.clone.twitter.project_service.dto.donation.DonationCreateDto;
import com.clone.twitter.project_service.dto.donation.DonationDto;
import com.clone.twitter.project_service.model.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DonationMapper {

    Donation toEntity(DonationCreateDto donationCreateDto);

    @Mapping(target = "campaignId", source = "campaign.id")
    DonationDto toDto(Donation donation);

    PaymentRequest toPaymentRequest(Donation donation);
}