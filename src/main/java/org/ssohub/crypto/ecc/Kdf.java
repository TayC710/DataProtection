/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

import static org.ssohub.crypto.ecc.libecc.ecc_kdf_argon2id;
import static org.ssohub.crypto.ecc.libecc.ecc_kdf_argon2id_SALTIZE;

/**
 * Key derivation functions.
 *
 * @author aldenml
 */
public final class Kdf {

    private Kdf() {
    }

    /**
     * See <a href="https://datatracker.ietf.org/doc/html/rfc9106">RFC9106</a>
     *
     * @param passphrase the passphrase to use as the input
     * @param salt       the salt to use, must be of size ecc_kdf_argon2id_SALTIZE
     * @param memorySize amount of memory (in kibibytes) to use
     * @param iterations number of passes
     * @param length     intended output length
     * @return the output on success or null if the computation didn't complete
     */
    public static Data argon2id(
        Data passphrase,
        Data salt,
        int memorySize,
        int iterations,
        int length
    ) {
        if (salt.size() != ecc_kdf_argon2id_SALTIZE)
            throw new IllegalArgumentException("salt length should be ecc_kdf_argon2id_SALTIZE");

        byte[] passphraseBytes = passphrase.toBytes();
        byte[] saltBytes = salt.toBytes();

        byte[] out = new byte[length];

        int r = ecc_kdf_argon2id(
            out,
            passphraseBytes, passphraseBytes.length,
            saltBytes,
            memorySize, iterations,
            length
        );

        return r == 0 ? new Data(out) : null;
    }
}
