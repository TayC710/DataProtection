package org.ssohub.crypto.ecc.ristretto255;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.ecc_ristretto255_SCALARSIZE;

public final class R255Scalar extends BaseData {

    public R255Scalar(Data data) {
        super(data, ecc_ristretto255_SCALARSIZE);
    }

    public static R255Scalar fromHex(String hex) {
        return new R255Scalar(Data.fromHex(hex));
    }
}
