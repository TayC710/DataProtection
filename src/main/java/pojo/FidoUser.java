package pojo;

import java.util.Arrays;

public class FidoUser {
    private int userId;
    private String userName;
    private byte[] userHandle;
    private String files;
    private String opaqueUsers;

    public FidoUser() {
    }

    @Override
    public String toString() {
        return "FidoUser{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userHandle=" + Arrays.toString(userHandle) +
                ", files='" + files + '\'' +
                ", opaqueUsers='" + opaqueUsers + '\'' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(byte[] userHandle) {
        this.userHandle = userHandle;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getOpaqueUsers() {
        return opaqueUsers;
    }

    public void setOpaqueUsers(String opaqueUsers) {
        this.opaqueUsers = opaqueUsers;
    }
}
