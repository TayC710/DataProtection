package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_Nn;

public final class OpaqueSeed extends BaseData {

    public OpaqueSeed(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_Nn);
    }

    public static OpaqueSeed fromHex(String hex) {
        return new OpaqueSeed(Data.fromHex(hex));
    }
}
