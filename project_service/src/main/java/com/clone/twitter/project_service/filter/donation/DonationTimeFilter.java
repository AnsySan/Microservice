package com.clone.twitter.project_service.filter.donation;

import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.model.Donation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Component
public class DonationTimeFilter implements DonationFilter {
    @Override
    public boolean isAcceptable(DonationFilterDto donationFilterDto) {
        return donationFilterDto.getDonationTime() != null;
    }

    @Override
    public Stream<Donation> applyFilter(Stream<Donation> donations, DonationFilterDto donationFilterDto) {
        LocalDate time = LocalDate.parse(donationFilterDto.getDonationTime(), DateTimeFormatter.ISO_DATE);
        return donations.filter(donation -> isEqual(donation, time));
    }

    private boolean isEqual(Donation donation, LocalDate time) {
        LocalDate donationTime = donation.getDonationTime().toLocalDate();
        return donationTime.isEqual(time);
    }
}
