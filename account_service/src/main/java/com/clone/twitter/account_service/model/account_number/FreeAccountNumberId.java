package com.clone.twitter.account_service.model.account_number;

import com.clone.twitter.account_service.model.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class FreeAccountNumberId {

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 32)
    private AccountType type;

    @Column(name = "number", nullable = false, length = 20)
    @Size(min = 12, max = 20, message = "The number account length must be from 12 to 20 characters.")
    private String number;
}
