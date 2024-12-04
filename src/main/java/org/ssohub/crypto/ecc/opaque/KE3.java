package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_KE3SIZE;

public final class KE3 extends BaseData {

    public KE3(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_KE3SIZE);
    }

    public static KE3 fromHex(String hex) {
        return new KE3(Data.fromHex(hex));
    }
}
