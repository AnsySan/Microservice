package com.clone.twitter.analytics_service.event.premium;

import com.clone.twitter.analytics_service.event.Event;
import com.clone.twitter.analytics_service.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class PremiumBoughtEvent implements Event {
    private long userId;
    @JsonProperty("amountOfMoney")
    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;
    private int premiumPeriod;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime boughtAt;
}
