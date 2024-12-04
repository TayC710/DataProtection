package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_KE1SIZE;

public final class KE1 extends BaseData {

    public KE1(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_KE1SIZE);
    }

    public static KE1 fromHex(String hex) {
        return new KE1(Data.fromHex(hex));
    }
}
