package com.clone.twitter.account_service.property;

import com.clone.twitter.account_service.model.enums.AccountType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "scheduling.account-number-generation")
public class AccountNumberGenerationAmountProperties {
    private Map<AccountType, Integer> amounts;
}
