package com.clone.twitter.project_service.service.donation.filter;

import com.clone.twitter.project_service.dto.donation.filter.DonationFilterDto;
import com.clone.twitter.project_service.filter.donation.DonationFilter;
import com.clone.twitter.project_service.model.Donation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonationFilterServiceImpl implements DonationFilterService {

    private final List<DonationFilter> filters;

    @Override
    public Stream<Donation> applyFilters(@NonNull Stream<Donation> donations, @NonNull DonationFilterDto filterDto) {
        for (DonationFilter filter : filters) {
            if (filter.isAcceptable(filterDto)) {
                donations = donations.flatMap(donation -> filter.applyFilter(Stream.of(donation), filterDto));
            }
        }
        return donations;
    }
}