package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_Nsk;

public final class OpaqueSk extends BaseData {

    public OpaqueSk(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_Nsk);
    }

    public static OpaqueSk fromHex(String hex) {
        return new OpaqueSk(Data.fromHex(hex));
    }
}
