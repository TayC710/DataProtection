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

import static org.ssohub.crypto.ecc.libecc.ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE;

/**
 * Request to send to the server in the OPAQUE protocol.
 *
 * @author aldenml
 */
public final class RegistrationRequest extends BaseData {

    /**
     * Create a request from raw data returned from the C api.
     *
     * @param data raw data from the C api.
     */
    public RegistrationRequest(Data data) {
        super(data, ecc_opaque_ristretto255_sha512_REGISTRATIONREQUESTSIZE);
    }

    /**
     * A serialized OPRF group element.
     */
    public R255Element getBlindedMessage() {
        return new R255Element(data);
    }

    public static RegistrationRequest fromHex(String hex) {
        return new RegistrationRequest(Data.fromHex(hex));
    }
}
