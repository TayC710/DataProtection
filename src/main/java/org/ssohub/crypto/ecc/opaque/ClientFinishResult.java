/*
 * Copyright (c) 2023, Alden Torres
 *
 * Licensed under the terms of the MIT license.
 * Copy of the license at https://opensource.org/licenses/MIT
 */

package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.Data;

public final class ClientFinishResult {

    private final KE3 ke3;

    private final Data sessionKey;

    private final Data exportKey;

    private final int result;

    public ClientFinishResult(KE3 ke3, Data sessionKey, Data exportKey, int result) {
        this.ke3 = ke3;
        this.sessionKey = sessionKey;
        this.exportKey = exportKey;
        this.result = result;
    }

    public KE3 getKE3() {
        return ke3;
    }

    public Data getSessionKey() {
        return sessionKey;
    }

    public Data getExportKey() {
        return exportKey;
    }

    public int getResult() {
        return result;
    }
}
