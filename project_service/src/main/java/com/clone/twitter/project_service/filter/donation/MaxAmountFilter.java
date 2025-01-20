package com.clone.twitter.project_service.filter.donation;

import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.model.Donation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Component
public class MaxAmountFilter implements DonationFilter {

    @Override
    public boolean isAcceptable(DonationFilterDto donationFilterDto) {
        return donationFilterDto.getMaxAmount() != null;
    }

    @Override
    public Stream<Donation> applyFilter(Stream<Donation> donations, DonationFilterDto donationFilterDto) {
        BigDecimal maxAmount = donationFilterDto.getMaxAmount();
        return donations.filter(donation -> donation.getAmount().compareTo(maxAmount) <= 0);
    }
}