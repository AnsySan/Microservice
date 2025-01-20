package com.clone.twitter.account_service.dto;

import com.clone.twitter.account_service.model.enums.OwnerType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnerDto {

    private Long id;

    private Long accountId;

    @NotNull(message = "Owner type must not be null")
    private OwnerType ownerType;
}