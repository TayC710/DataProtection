package util;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class VerificateUtils {
    private String sender;
    private String recipient = "2312225254@qq.com";
    private String authentication = "pdbxzenvifrweadi";
    private String hostName = "smtp.qq.com";
    private String title = "【轻量级数据安全系统】注册验证码";
    private int codeLength = 4;
    private Email email = new SimpleEmail();

    public VerificateUtils() throws EmailException {
        email.setHostName(hostName);
        email.setCharset("utf-8");
        email.setFrom(recipient, "Admin");
        email.setAuthentication(recipient, authentication);
    }

    public String send(String to) throws EmailException {
        email.addTo(to);
        email.setSubject(title);
        String code = generateRandomCode();
        email.setMsg("你好,您的轻量级数据安全系统,验证码为：" + code);
        email.send();
        return code;
    }

    public String generateRandomCode() {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        while (code.length() < codeLength) {
            int index = (new java.util.Random()).nextInt(s.length());
            Character ch = s.charAt(index);
            if (code.indexOf(ch.toString()) < 0) {
                code.append(ch);
            }
        }
        return code.toString();
    }
}
