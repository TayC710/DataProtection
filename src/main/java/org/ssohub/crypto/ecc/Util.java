/*
 * Copyright (c) 2021-2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utility functions.
 *
 * @author aldenml
 */
public final class Util {

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_HEX =
        {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Util() {
    }

    /**
     * Converts a string into a byte array using UTF-8 encoding.
     *
     * @param s the input string
     * @return the UTF-8 encoding bytes
     */
    public static byte[] str2bin(String s) {
        return s.getBytes(UTF_8);
    }

    /**
     * Converts a byte array to the hex string.
     *
     * @param bin the input byte array
     * @return the hex encoded string
     */
    public static String bin2hex(byte[] bin, int offset, int length) {
        final char[] out = new char[length << 1];
        for (int i = offset, j = 0; i < length; i++) {
            out[j++] = DIGITS_HEX[(0xF0 & bin[i]) >>> 4];
            out[j++] = DIGITS_HEX[0x0F & bin[i]];
        }
        return new String(out);
    }

    /**
     * Converts a byte array to the hex string.
     *
     * @param bin the input byte array
     * @return the hex encoded string
     */
    public static String bin2hex(byte[] bin) {
        return bin2hex(bin, 0, bin.length);
    }

    /**
     * Converts a hex string to a byte array.
     *
     * @param hex the input hex string
     * @return the byte array
     */
    public static byte[] hex2bin(String hex) {

        final int len = hex.length();

        final byte[] out = new byte[len >> 1];

        for (int i = 0, j = 0; j < len; i++) {
            int digit = Character.digit(hex.charAt(j), 16);
            int f = digit << 4;
            j++;
            digit = Character.digit(hex.charAt(j), 16);
            f = f | digit;
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Returns a buffer of length `n` with an unpredictable sequence of bytes.
     *
     * @param n the length of the buffer to return
     * @return the buffer with random elements
     */
    public static byte[] randomBytes(int n) {
        byte[] buf = new byte[n];
        libecc.ecc_randombytes(buf, n);

        return buf;
    }

    public static Data randomData(int n) {
        return new Data(randomBytes(n));
    }

    public static String randomUTF8(int n) {
        return new String(randomBytes(n), UTF_8);
    }

    public static String randomAlphaNum(int n) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(n);
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            char ch = alphabet.charAt(rnd.nextInt(alphabet.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
