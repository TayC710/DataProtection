/*
 * Copyright (c) 2022-2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc;

import static org.ssohub.crypto.ecc.libecc.*;

/**
 * @author aldenml
 */
public final class Oprf {

    private Oprf() {
    }

    public static byte[] oprf_ristretto255_sha512_Evaluate(
        byte[] skS,
        byte[] blindedElement
    ) {

        byte[] evaluatedElement = new byte[ecc_voprf_ristretto255_sha512_ELEMENTSIZE];
        ecc_voprf_ristretto255_sha512_BlindEvaluate(
            evaluatedElement,
            skS,
            blindedElement
        );

        return evaluatedElement;
    }

    public static byte[] oprf_ristretto255_sha512_BlindWithScalar(
        byte[] input,
        byte[] blind
    ) {
        byte[] blindedElement = new byte[ecc_voprf_ristretto255_sha512_ELEMENTSIZE];
        ecc_voprf_ristretto255_sha512_BlindWithScalar(
            blindedElement,
            input, input.length,
            blind,
            ecc_voprf_ristretto255_sha512_MODE_OPRF
        );

        return blindedElement;
    }

    public static byte[] oprf_ristretto255_sha512_Finalize(
        byte[] input,
        byte[] blind,
        byte[] evaluatedElement
    ) {

        byte[] output = new byte[ecc_voprf_ristretto255_sha512_Nh];
        ecc_voprf_ristretto255_sha512_Finalize(
            output,
            input, input.length,
            blind,
            evaluatedElement
        );

        return output;
    }
}
