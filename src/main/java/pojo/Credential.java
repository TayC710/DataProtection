package pojo;

import java.util.Arrays;

public class Credential {
    private int id;
    private byte[] credentialId;
    private int userId;
    private byte[] userHandle;
    private byte[] publicKeyCose;
    private int signatureCount;

    public Credential() {
    }

    public Credential(byte[] credentialId, int userId, byte[] userHandle, byte[] publicKeyCose, int signatureCount) {
        this.credentialId = credentialId;
        this.userId = userId;
        this.userHandle = userHandle;
        this.publicKeyCose = publicKeyCose;
        this.signatureCount = signatureCount;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", credentialId=" + Arrays.toString(credentialId) +
                ", userId=" + userId +
                ", userHandle=" + Arrays.toString(userHandle) +
                ", publicKeyCose=" + Arrays.toString(publicKeyCose) +
                ", signatureCount=" + signatureCount +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(byte[] credentialId) {
        this.credentialId = credentialId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(byte[] userHandle) {
        this.userHandle = userHandle;
    }

    public byte[] getPublicKeyCose() {
        return publicKeyCose;
    }

    public void setPublicKeyCose(byte[] publicKeyCose) {
        this.publicKeyCose = publicKeyCose;
    }

    public int getSignatureCount() {
        return signatureCount;
    }

    public void setSignatureCount(int signatureCount) {
        this.signatureCount = signatureCount;
    }
}
