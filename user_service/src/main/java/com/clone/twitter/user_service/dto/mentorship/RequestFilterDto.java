package com.clone.twitter.user_service.dto.mentorship;


import com.clone.twitter.user_service.entity.RequestStatus;
import com.clone.twitter.user_service.validator.enumvalidator.EnumValidator;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestFilterDto {

    @Size(min = 20, max = 4096, message = "description should be more then 19 and less or equal to 4096 symbols")
    private String descriptionPattern;

    @Positive
    private Long requesterIdPattern;

    @Positive
    private Long receiverIdPattern;

    @EnumValidator(enumClass = RequestStatus.class, message = "Invalid Request Status")
    private RequestStatus statusPattern;
}
