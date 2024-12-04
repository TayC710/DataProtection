/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

public final class GenerateAuthKeyPairResult {

    private final OpaqueSk privateKey;

    private final OpaquePk publicKey;

    public GenerateAuthKeyPairResult(OpaqueSk privateKey, OpaquePk publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public OpaqueSk getPrivateKey() {
        return privateKey;
    }

    public OpaquePk getPublicKey() {
        return publicKey;
    }
}
