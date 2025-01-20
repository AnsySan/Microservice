package com.clone.twitter.account_service.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountNumberUtil {

    public String getAccountNumber(int code, int length, long count) {

        validatePositive(code, length, count);

        String codeStr = String.valueOf(code);
        String countStr = String.valueOf(count);

        StringBuilder builder = new StringBuilder();
        builder.append(codeStr);

        int dif = length - codeStr.length() - countStr.length();

        if (dif < 0) {
            throw new IllegalArgumentException("Count is greater than length");
        }

        builder.append("0".repeat(dif));
        builder.append(count);

        return builder.toString();
    }

    private void validatePositive(int code, int length, long count) {
        if (code < 0) {
            throw new IllegalArgumentException("Code must be greater that zero");
        } else if (length < 0) {
            throw new IllegalArgumentException("Length must be greater that zero");
        } else if (count < 0) {
            throw new IllegalArgumentException("count must be greater that zero");
        }
    }
}
