package com.clone.twitter.post_service.service.ad;

import com.clone.twitter.post_service.repository.ad.AdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;

    @Override
    @Async("adRemoverExecutorService")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAds(List<Long> adIds) {
        adRepository.deleteAllById(adIds);
    }
}
