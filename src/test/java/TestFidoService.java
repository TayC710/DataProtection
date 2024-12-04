import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.data.exception.Base64UrlException;
import org.apache.commons.mail.EmailException;
import org.junit.Test;
import service.FidoService;
import util.VerificateUtils;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class TestFidoService {
    private static final Base64.Decoder BASE64URL_DECODER = Base64.getUrlDecoder();

    @Test
    public void testIsRegistered() {
        FidoService fidoService = new FidoService();

        boolean result = fidoService.isRegistered("Liu");
        System.out.println("[+] TestFidoService.testIsRegistered: " + result);

        result = fidoService.isRegistered("Xie");
        System.out.println("[+] TestFidoService.testIsRegistered: " + result);
    }

    @Test
    public void testRegisterFidoUser() {
        FidoService fidoService = new FidoService();

        byte[] testPubKey = new byte[]{1, 2, 3};

        System.out.println(Arrays.toString(testPubKey));

        fidoService.RegisterFidoUser("Xie", testPubKey);
    }

//    @Test
//    public void testRegisterNewUser() throws IOException {
//        FidoService fidoService = new FidoService();
//        fidoService.registerNewUser("Liu");
//        System.out.println("@Test.testRegisterNewUser:");
//    }

    @Test
    public void testBase64URL() throws Base64UrlException {
        String base64url = "Oym_q0tw5mqD8iXc9RAEBL3-W0t-c9IF0Rqy0Mf0TYU";
        try {
            System.out.println(Arrays.toString(BASE64URL_DECODER.decode(base64url)));
        } catch (IllegalArgumentException var3) {
            throw new Base64UrlException("Invalid Base64Url encoding: " + base64url, var3);
        }
    }
    @Test
    public void testLength(){
        String t = "Oym_q0tw5mqD8iXc9RAEBL3-W0t-c9IF0Rqy0Mf0TYU";
        System.out.println(t.length());
    }
    @Test
    public void testString(){
        String inputData = "{\"username\":\"123\"}";
        System.out.println(inputData.replace("\"","\\"+"\""));
    }
    @Test
    public void testVerificate() throws EmailException {
        VerificateUtils verificateUtils = new VerificateUtils();
        String email = "xiesiyuan2002@foxmail.com";
        String code = verificateUtils.send(email);
    }

}
