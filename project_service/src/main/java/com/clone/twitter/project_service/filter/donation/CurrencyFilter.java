package com.clone.twitter.project_service.filter.donation;

import com.clone.twitter.project_service.dto.client.Currency;
import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.model.Donation;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class CurrencyFilter implements DonationFilter{
    @Override
    public boolean isAcceptable(DonationFilterDto donationFilterDto) {
        return donationFilterDto.getCurrency() != null;
    }

    @Override
    public Stream<Donation> applyFilter(Stream<Donation> donations, DonationFilterDto donationFilterDto) {
        return donations.filter(donation -> donation.getCurrency().equals(Currency.valueOf(donationFilterDto.getCurrency())));
    }
}
