package com.clone.twitter.project_service.service.moment;

import com.clone.twitter.project_service.dto.moment.MomentDto;
import com.clone.twitter.project_service.model.initiative.Initiative;

public interface MomentService {
    MomentDto create(MomentDto momentDto);

    MomentDto createFromInitiative(Initiative initiative);
}
