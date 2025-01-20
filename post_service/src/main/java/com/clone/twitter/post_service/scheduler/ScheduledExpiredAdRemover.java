package com.clone.twitter.post_service.scheduler;

import com.clone.twitter.post_service.repository.ad.AdRepository;
import com.clone.twitter.post_service.service.ad.AdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledExpiredAdRemover {

    @Value("${post.ad-remover.max-list-size}")
    private int maxListSize;
    private final AdService adService;
    private final AdRepository adRepository;

    @Scheduled(cron = "${post.ad-remover.scheduler.cron}")
    public void deleteExpiredAdsScheduled() {

        List<Long> adsToDelete = adRepository.findAllExpiredIds();

        if (adsToDelete.isEmpty()) {
            log.info("No expired ads to remove");
            return;
        }

        List<List<Long>> partitionedAdsToDelete = ListUtils.partition(adsToDelete, maxListSize);

        partitionedAdsToDelete.forEach(adService::deleteAds);

        log.info("Expired ads are removed");
    }
}
