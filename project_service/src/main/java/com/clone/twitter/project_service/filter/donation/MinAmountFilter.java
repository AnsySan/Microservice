package com.clone.twitter.project_service.filter.donation;

import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.model.Donation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Component
public class MinAmountFilter implements DonationFilter{

    @Override
    public boolean isAcceptable(DonationFilterDto donationFilterDto) {
        return donationFilterDto.getMinAmount() != null;
    }

    @Override
    public Stream<Donation> applyFilter(Stream<Donation> donations, DonationFilterDto donationFilterDto) {
        BigDecimal minAmount = donationFilterDto.getMinAmount();
        return donations.filter(donation -> donation.getAmount().compareTo(minAmount) >= 0);
    }
}