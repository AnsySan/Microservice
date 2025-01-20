package com.clone.twitter.project_service.dto.internship;

import com.clone.twitter.project_service.model.InternshipStatus;
import lombok.Data;

@Data
public class InternshipFilterDto {
    private InternshipStatus status;
}
