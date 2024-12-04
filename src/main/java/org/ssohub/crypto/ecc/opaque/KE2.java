package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_KE2SIZE;

public final class KE2 extends BaseData {

    public KE2(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_KE2SIZE);
    }

    public static KE2 fromHex(String hex) {
        return new KE2(Data.fromHex(hex));
    }
}
