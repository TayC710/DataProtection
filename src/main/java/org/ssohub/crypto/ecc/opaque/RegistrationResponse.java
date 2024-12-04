/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.BaseData;
import org.ssohub.crypto.ecc.Data;
import org.ssohub.crypto.ecc.ristretto255.R255Element;

import static org.ssohub.crypto.ecc.libecc.*;

/**
 * Response to send to the client in the OPAQUE protocol.
 *
 * @author aldenml
 */
public final class RegistrationResponse extends BaseData {

    /**
     * Create a request from raw data returned from the C api.
     *
     * @param data raw data from the C api.
     */
    public RegistrationResponse(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_REGISTRATIONRESPONSESIZE);
    }

    /**
     * A serialized OPRF group element.
     */
    public R255Element getEvaluatedMessage() {
        return new R255Element(data.copy(0, ecc_opaque_ristretto255_sha512_Noe));
    }

    /**
     * The server's encoded public key that will be used for the online AKE stage.
     */
    public OpaquePk getServerPublicKey() {
        return new OpaquePk(data.copy(ecc_opaque_ristretto255_sha512_Noe, ecc_opaque_ristretto255_sha512_Npk));
    }

    public static RegistrationResponse fromHex(String hex) {
        return new RegistrationResponse(Data.fromHex(hex));
    }
}
