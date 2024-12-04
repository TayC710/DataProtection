/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Provide an immutable interface to a byte buffer.
 * <p>
 * There is no facility to clear the memory, in the JVM environment
 * you need to assume that effective handling of secrets in memory
 * is not possible. If you need to be careful with portions of
 * memory, implement that part in native C and never expose it to
 * the JVM.
 *
 * @author aldenml
 */
public final class Data implements DataLike {

    private final byte[] arr;

    /**
     * Internally perform a copy of the array.
     */
    public Data(byte[] arr, int offset, int length) {
        if (offset < 0 || length < 0)
            throw new IllegalArgumentException("both offset and length should be > 0");
        this.arr = Arrays.copyOfRange(arr, offset, offset + length);
    }

    /**
     * Internally perform a copy of the array.
     */
    public Data(byte[] arr) {
        this(arr, 0, arr.length);
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public byte get(int index) {
        return arr[index];
    }

    @Override
    public String toHex() {
        return Util.bin2hex(arr);
    }

    @Override
    public byte[] toBytes() {
        return Arrays.copyOf(arr, arr.length);
    }

    public String toUTF8() {
        return new String(arr, UTF_8);
    }

    public Data copy(int offset, int length) {
        return new Data(arr, offset, length);
    }

    public Data copy() {
        return new Data(arr);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Data))
            return false;
        if (this == obj)
            return true;

        return Arrays.equals(arr, ((Data) obj).arr);
    }

    public static Data fromHex(String hex) {
        return new Data(Util.hex2bin(hex));
    }
}
