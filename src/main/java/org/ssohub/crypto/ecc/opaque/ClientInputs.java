/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.Data;

public final class ClientInputs {

    private final Data serverIdentity;

    private final Data clientIdentity;

    private final Data password;

    public ClientInputs(Data serverIdentity, Data clientIdentity, Data password) {
        this.serverIdentity = serverIdentity;
        this.clientIdentity = clientIdentity;
        this.password = password;
    }

    public Data getServerIdentity() {
        return serverIdentity;
    }

    public Data getClientIdentity() {
        return clientIdentity;
    }

    public Data getPassword() {
        return password;
    }
}
