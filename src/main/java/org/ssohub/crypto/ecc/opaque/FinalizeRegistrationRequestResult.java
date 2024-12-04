package org.ssohub.crypto.ecc.opaque;

import org.ssohub.crypto.ecc.Data;

public final class FinalizeRegistrationRequestResult {

    private final RegistrationRecord registrationRecord;
    private final Data exportKey;

    public FinalizeRegistrationRequestResult(RegistrationRecord registrationRecord, Data exportKey) {
        this.registrationRecord = registrationRecord;
        this.exportKey = exportKey;
    }

    public RegistrationRecord getRegistrationRecord() {
        return registrationRecord;
    }

    public Data getExportKey() {
        return exportKey;
    }
}
