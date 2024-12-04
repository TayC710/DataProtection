package pojo;

public class FidoRegisterResponse {
    private String challenge;
    private String rpName;
    private String rpID;

    public FidoRegisterResponse(String challenge, String rpName, String rpID) {
        this.challenge = challenge;
        this.rpName = rpName;
        this.rpID = rpID;
    }

    public String getRpName() {
        return rpName;
    }

    public void setRpName(String rpName) {
        this.rpName = rpName;
    }

    public String getRpID() {
        return rpID;
    }

    public void setRpID(String rpID) {
        this.rpID = rpID;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
