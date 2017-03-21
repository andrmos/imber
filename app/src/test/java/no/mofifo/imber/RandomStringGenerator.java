package com.mossige.finseth.follo.inf219_mitt_uib;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Follo on 10.05.2016.
 */
public final class RandomStringGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
