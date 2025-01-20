package com.clone.twitter.project_service.service.donation.filter;


import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.model.Donation;

import java.util.stream.Stream;

public interface DonationFilterService {
    Stream<Donation> applyFilters(Stream<Donation> donations, DonationFilterDto filterDto);
}