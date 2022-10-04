package com.client.eurekaclient.utilities.password.generator;

import java.util.Random;

public class RandomPasswordGenerator {
    public static String generateRandomPassword() {
        return new Random().ints(24, 33, 122).collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
