package com.clone.twitter.analytics_service.model;

import java.time.LocalDateTime;

public enum Interval {

    YEAR,
    MONTH,
    WEEK,
    DAY;

    public static LocalDateTime getFromDate(Interval interval) {
        return switch (interval) {
            case YEAR -> LocalDateTime.now().minusYears(1);
            case MONTH -> LocalDateTime.now().minusMonths(1);
            case WEEK -> LocalDateTime.now().minusWeeks(1);
            case DAY -> LocalDateTime.now().minusDays(1);
        };
    }
}
