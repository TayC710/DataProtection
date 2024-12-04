package pojo;

import java.util.Arrays;

public class Files {
    private byte[] credentialIdentifier;
    private byte[] clientIdentity;
    private byte[] record;
    private String path;

    public Files(byte[] credentialIdentifier, byte[] clientIdentity, byte[] record) {
        this.credentialIdentifier = credentialIdentifier;
        this.clientIdentity = clientIdentity;
        this.record = record;
    }

    @Override
    public String toString() {
        return "Files{" +
                "credentialIdentifier=" + Arrays.toString(credentialIdentifier) +
                ", clientIdentity=" + Arrays.toString(clientIdentity) +
                ", record=" + Arrays.toString(record) +
                ", path='" + path + '\'' +
                '}';
    }

    public byte[] getCredentialIdentifier() {
        return credentialIdentifier;
    }

    public void setCredentialIdentifier(byte[] credentialIdentifier) {
        this.credentialIdentifier = credentialIdentifier;
    }

    public byte[] getClientIdentity() {
        return clientIdentity;
    }

    public void setClientIdentity(byte[] clientIdentity) {
        this.clientIdentity = clientIdentity;
    }

    public byte[] getRecord() {
        return record;
    }

    public void setRecord(byte[] record) {
        this.record = record;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
