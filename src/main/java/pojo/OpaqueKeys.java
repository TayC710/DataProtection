package pojo;

import java.util.Arrays;

public class OpaqueKeys {
    private byte[] serverPublicKey;
    private byte[] serverPrivateKey;
    private byte[] oprfSeed;

    public byte[] getServerPublicKey() {
        return serverPublicKey;
    }

    public void setServerPublicKey(byte[] serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }

    @Override
    public String toString() {
        return "OpaqueKeys{\n" +
                "    serverPublicKey=" + Arrays.toString(serverPublicKey) +
                ",\n    serverPrivateKey=" + Arrays.toString(serverPrivateKey) +
                ",\n    oprfSeed=" + Arrays.toString(oprfSeed) +
                "\n    }";
    }

    public byte[] getServerPrivateKey() {
        return serverPrivateKey;
    }

    public void setServerPrivateKey(byte[] serverPrivateKey) {
        this.serverPrivateKey = serverPrivateKey;
    }

    public byte[] getOprfSeed() {
        return oprfSeed;
    }

    public void setOprfSeed(byte[] oprfSeed) {
        this.oprfSeed = oprfSeed;
    }
}
