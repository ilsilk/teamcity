package com.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomData {

    private static final int LENGTH = 10;

    private RandomData() {
    }

    public static String getString() {
        return "test_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

}
