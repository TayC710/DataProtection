package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.Data;

public final class ServerFinishResult {

    private final Data sessionKey;

    private final int result;

    public ServerFinishResult(Data sessionKey, int result) {
        this.sessionKey = sessionKey;
        this.result = result;
    }

    public Data getSessionKey() {
        return sessionKey;
    }

    public int getResult() {
        return result;
    }
}
