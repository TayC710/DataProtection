package pojo;

import java.util.Arrays;

public class OpaqueUser {
    private int userId;
    private byte[] clientIdentity;
    private byte[] password;
    private byte[] serverIdentity;
    private byte[] blind;
    private byte[] nonce;
    private byte[] clientPublicKey;
    private byte[] clientPrivateKey;

    public OpaqueUser(byte[] clientIdentity, byte[] password, byte[] serverIdentity, byte[] blind, byte[] nonce, byte[] clientPublicKey, byte[] clientPrivateKey) {
        this.clientIdentity = clientIdentity;
        this.password = password;
        this.serverIdentity = serverIdentity;
        this.blind = blind;
        this.nonce = nonce;
        this.clientPublicKey = clientPublicKey;
        this.clientPrivateKey = clientPrivateKey;
    }

    @Override
    public String toString() {
        return "OpaqueUser{" +
                "userId=" + userId +
                ", clientIdentity=" + Arrays.toString(clientIdentity) +
                ", password=" + Arrays.toString(password) +
                ", serverIdentity=" + Arrays.toString(serverIdentity) +
                ", blind=" + Arrays.toString(blind) +
                ", nonce=" + Arrays.toString(nonce) +
                ", clientPublicKey=" + Arrays.toString(clientPublicKey) +
                ", clientPrivateKey=" + Arrays.toString(clientPrivateKey) +
                '}';
    }

    public byte[] getClientIdentity() {
        return clientIdentity;
    }

    public void setClientIdentity(byte[] clientIdentity) {
        this.clientIdentity = clientIdentity;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getServerIdentity() {
        return serverIdentity;
    }

    public void setServerIdentity(byte[] serverIdentity) {
        this.serverIdentity = serverIdentity;
    }

    public byte[] getBlind() {
        return blind;
    }

    public void setBlind(byte[] blind) {
        this.blind = blind;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] getClientPublicKey() {
        return clientPublicKey;
    }

    public void setClientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }

    public byte[] getClientPrivateKey() {
        return clientPrivateKey;
    }

    public void setClientPrivateKey(byte[] clientPrivateKey) {
        this.clientPrivateKey = clientPrivateKey;
    }
}
