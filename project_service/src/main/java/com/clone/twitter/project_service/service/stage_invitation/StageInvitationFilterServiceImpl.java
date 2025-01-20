package com.clone.twitter.project_service.service.stage_invitation;

import com.clone.twitter.project_service.dto.stage_invitation.StageInvitationFilterDto;
import com.clone.twitter.project_service.model.stage_invitation.StageInvitation;
import com.clone.twitter.project_service.service.stage_invitation.filter.StageInvitationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StageInvitationFilterServiceImpl implements StageInvitationFilterService {

    private final List<StageInvitationFilter> filters;

    @Override
    public Stream<StageInvitation> applyAll(Stream<StageInvitation> stageInvitations, StageInvitationFilterDto filterDto) {
        return filters.stream()
                .filter(filter -> filter.isAcceptable(filterDto))
                .flatMap(filter -> filter.apply(stageInvitations, filterDto));
    }
}
