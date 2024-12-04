package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.DataLike;
import org.ssohub.crypto.ecc.Util;

import java.util.Arrays;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_SERVERSTATESIZE;

public final class ServerState implements DataLike {

    private final byte[] data;

    public ServerState(byte[] data) {
        if (data.length != ecc_opaque_ristretto255_sha512_SERVERSTATESIZE)
            throw new IllegalArgumentException("data should be of size: " + ecc_opaque_ristretto255_sha512_SERVERSTATESIZE);

        this.data = data;
    }

    public ServerState() {
        this(new byte[ecc_opaque_ristretto255_sha512_SERVERSTATESIZE]);
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

    public static ServerState fromHex(String hex) {
        return new ServerState(Util.hex2bin(hex));
    }
}
