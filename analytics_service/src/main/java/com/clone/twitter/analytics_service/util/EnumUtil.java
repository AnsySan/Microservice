package com.clone.twitter.analytics_service.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnumUtil {

    public static <T extends Enum<T>> T getEnum(@NonNull Class<T> enumClass, @NonNull String enumType) {
        try {
            return Enum.valueOf(enumClass, enumType);
        } catch (IllegalArgumentException e) {
            return of(enumClass, enumType);
        }
    }

    public static <T extends Enum<T>> T of(@NonNull Class<T> enumClass, @NonNull String enumType) {

        int num;
        try {
            num = Integer.parseInt(enumType);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Can't parse enum: " + e);
        }

        if (num < 0 || num >= enumClass.getEnumConstants().length) {
            throw new IllegalArgumentException("Invalid enumType: " + enumType);
        }

        for (T e : enumClass.getEnumConstants()) {
            if (e.ordinal() == num) {
                return e;
            }
        }

        throw new IllegalArgumentException("Unknown enum: " + num);
    }
}
