package com.kursach;

import com.kursach.KaratsubaMultiplication;

import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        BigInteger a = new BigInteger("3141592653589793238462643383279502884197169399375105820974944592");
        BigInteger b = new BigInteger("2718281828459045235360287471352662497775724270957799666");
        System.out.println(KaratsubaMultiplication.multiply(a, b));
    }
}
