package com.kursach;

import java.math.BigInteger;

public final class KaratsubaMultiplication {
    // Requirement: CUTOFF >= 64, or else there will be infinite recursion.
    private static final int CUTOFF = 1536;
    public static BigInteger multiply(BigInteger x, BigInteger y) {
        if (x.bitLength() <= CUTOFF || y.bitLength() <= CUTOFF) {  // Base case
            return x.multiply(y);
        } else {
            int n = Math.max(x.bitLength(), y.bitLength());
            int half = (n + 32) / 64 * 32;  // Number of bits to use for the low part
            BigInteger mask = BigInteger.ONE.shiftLeft(half).subtract(BigInteger.ONE);
            BigInteger xlow = x.and(mask);
            BigInteger ylow = y.and(mask);
            BigInteger xhigh = x.shiftRight(half);
            BigInteger yhigh = y.shiftRight(half);

            BigInteger a = multiply(xhigh, yhigh);
            BigInteger b = multiply(xlow.add(xhigh), ylow.add(yhigh));
            BigInteger c = multiply(xlow, ylow);
            BigInteger d = b.subtract(a).subtract(c);
            return a.shiftLeft(half).add(d).shiftLeft(half).add(c);
        }
    }
}