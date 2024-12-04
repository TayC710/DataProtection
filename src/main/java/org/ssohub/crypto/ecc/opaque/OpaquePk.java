package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_Npk;

public final class OpaquePk extends BaseData {

    public OpaquePk(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_Npk);
    }

    public static OpaquePk fromHex(String hex) {
        return new OpaquePk(Data.fromHex(hex));
    }
}
