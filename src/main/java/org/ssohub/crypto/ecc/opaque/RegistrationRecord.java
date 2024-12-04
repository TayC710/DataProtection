/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;

import static org.ssohub.crypto.ecc.libecc.*;

/**
 * @author aldenml
 */
public final class RegistrationRecord extends BaseData {

    /**
     * Create a request from raw data returned from the C api.
     *
     * @param data raw data from the C api.
     */
    public RegistrationRecord(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_REGISTRATIONRECORDSIZE);
    }

    /**
     * A serialized OPRF group element.
     */
    public OpaquePk getClientPublicKey() {
        return new OpaquePk(data.copy(0, ecc_opaque_ristretto255_sha512_Npk));
    }

    public Data getMaskingKey() {
        return data.copy(ecc_opaque_ristretto255_sha512_Npk, ecc_opaque_ristretto255_sha512_Nh);
    }

    public static RegistrationRecord fromHex(String hex) {
        return new RegistrationRecord(Data.fromHex(hex));
    }
}
