/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.DataLike;
import org.ssohub.crypto.ecc.Util;

import java.util.Arrays;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE;

public final class ClientState implements DataLike {

    private final byte[] data;

    public ClientState(byte[] data) {
        if (data.length != ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE)
            throw new IllegalArgumentException("data should be of size: " + ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE);

        this.data = data;
    }

    public ClientState() {
        this(new byte[ecc_opaque_ristretto255_sha512_CLIENTSTATESIZE]);
    }

    byte[] data() {
        return data;
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public byte get(int index) {
        return data[index];
    }

    @Override
    public String toHex() {
        return Util.bin2hex(data);
    }

    @Override
    public byte[] toBytes() {
        return Arrays.copyOf(data, data.length);
    }

    public static ClientState fromHex(String hex) {
        return new ClientState(Util.hex2bin(hex));
    }
}
