package com.clone.twitter.project_service.filter.donation;

import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.model.Donation;

import java.util.stream.Stream;

public interface DonationFilter {

    boolean isAcceptable(DonationFilterDto donationFilterDto);

    Stream<Donation> applyFilter(Stream<Donation> donations, DonationFilterDto donationFilterDto);
}