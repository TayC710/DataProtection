/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.ristretto255.R255Scalar;

public final class CreateRegistrationRequestResult {

    private final RegistrationRequest registrationRequest;
    private final R255Scalar blind;

    public CreateRegistrationRequestResult(RegistrationRequest registrationRequest, R255Scalar blind) {
        this.registrationRequest = registrationRequest;
        this.blind = blind;
    }

    public RegistrationRequest getRegistrationRequest() {
        return registrationRequest;
    }

    public R255Scalar getBlind() {
        return blind;
    }
}
