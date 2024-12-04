/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.Data;

public final class ServerInputs {

    private final OpaqueSk serverPrivateKey;

    private final OpaquePk serverPublicKey;

    private final Data credentialIdentifier;

    private final Data serverIdentity;

    private final Data clientIdentity;

    private final Data oprfSeed;

    public ServerInputs(
        OpaqueSk serverPrivateKey, OpaquePk serverPublicKey,
        Data credentialIdentifier,
        Data serverIdentity,
        Data clientIdentity,
        Data oprfSeed
    ) {
        this.serverPrivateKey = serverPrivateKey;
        this.serverPublicKey = serverPublicKey;
        this.credentialIdentifier = credentialIdentifier;
        this.serverIdentity = serverIdentity;
        this.clientIdentity = clientIdentity;
        this.oprfSeed = oprfSeed;
    }

    public OpaqueSk getServerPrivateKey() {
        return serverPrivateKey;
    }

    public OpaquePk getServerPublicKey() {
        return serverPublicKey;
    }

    public Data getCredentialIdentifier() {
        return credentialIdentifier;
    }

    public Data getServerIdentity() {
        return serverIdentity;
    }

    public Data getClientIdentity() {
        return clientIdentity;
    }

    public Data getOprfSeed() {
        return oprfSeed;
    }
}
