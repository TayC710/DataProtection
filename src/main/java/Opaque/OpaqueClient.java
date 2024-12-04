package Opaque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssohub.crypto.ecc.Data;
import org.ssohub.crypto.ecc.Util;
import org.ssohub.crypto.ecc.opaque.*;
import org.ssohub.crypto.ecc.ristretto255.R255Scalar;
import pojo.OpaqueUser;
import service.OpaqueService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.ssohub.crypto.ecc.Util.randomData;
import static org.ssohub.crypto.ecc.opaque.Opaque.*;

public class OpaqueClient {
    private final static Logger logger = LoggerFactory.getLogger(OpaqueClient.class);
    private static final String serverAddress = "localhost"; // 服务器地址
    private static final int serverPort = 9000; // 服务器端口号
    private static final int REGISTER = 0;
    private static final int LOGIN = 1;

    public static void startRegister(String fidoUserName, byte[] file) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // 创建Socket对象，并连接到服务器
        Socket socket = new Socket(serverAddress, serverPort);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        OpaqueService opaqueService = new OpaqueService();

        // 发送注册option，开始注册通信
        logger.info("start register");
        out.println(REGISTER);

        // 生成公私钥对以及随机的20位密码
        GenerateAuthKeyPairResult keyPair = generateAuthKeyPair();
        Data password = new Data(Util.str2bin(Util.randomAlphaNum(20)));

        // 接收服务器的公钥作为identity
        Data serverIdentity = Data.fromHex(in.readLine());
        logger.info("server identity received");

        // 向服务器发送公钥作为identity
        logger.info("sending client public key as identity");
        Data clientIdentity = Data.fromHex(keyPair.getPublicKey().toHex());
        out.println(clientIdentity.toHex());

        ClientInputs clientInputs = new ClientInputs(serverIdentity, clientIdentity, password);

        // 生成注册请求
        CreateRegistrationRequestResult request = createRegistrationRequest(clientInputs.getPassword());
        RegistrationRequest registrationRequest = request.getRegistrationRequest();

        R255Scalar blind = request.getBlind();

        // 发送注册请求
        logger.info("sending registration request");
        out.println(registrationRequest.toHex());

        // 接收response
        RegistrationResponse response = RegistrationResponse.fromHex(in.readLine());
        logger.info("registration response received");

        // 随机生成nonce
        OpaqueSeed envelopeNonce = OpaqueSeed.fromHex(randomData(32).toHex());

        // finalizeRegistrationRequestWithNonce(clientInputs.getPassword(), blind, response, clientInputs.getServerIdentity(), clientInputs.getClientIdentity(), MHF.IDENTITY, null, envelopeNonce);
        FinalizeRegistrationRequestResult finalizeRequest = finalizeRegistrationRequest(
                clientInputs.getPassword(),
                blind,
                response,
                clientInputs.getServerIdentity(),
                clientInputs.getClientIdentity(),
                MHF.IDENTITY,
                null);

        // 生成record、exportKey
        RegistrationRecord registrationRecord = finalizeRequest.getRegistrationRecord();
        Data exportKey = finalizeRequest.getExportKey();

        // 返回record
        logger.info("sending registration record");
        out.println(registrationRecord.toHex());

        logger.info("sending encrypted file");
        Data encryptedFile = opaqueService.encrypt(file, exportKey.toBytes());

        // Data encData = opaqueService.encrypt(encryptedFile.toBytes(), registrationRecord.getMaskingKey().toBytes());
        out.println(encryptedFile.toHex());

        out.close();
        in.close();

        // 持久化
        opaqueService.storeClientInfo(clientInputs, blind, envelopeNonce, keyPair, fidoUserName);
    }

    public static byte[] startLoginAndDownload(OpaqueUser opaqueUser) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // 创建Socket对象，并连接到服务器
        Socket socket = new Socket(serverAddress, serverPort);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        OpaqueService opaqueService = new OpaqueService();

        // 发送登录请求，开始登录
        logger.info("start log in");
        out.println(LOGIN);

        ClientState clientState = new ClientState();
        ClientInputs clientInputs = new ClientInputs(
                new Data(opaqueUser.getServerIdentity()),
                new Data(opaqueUser.getClientIdentity()),
                new Data(opaqueUser.getPassword())
        );

        // 发送client identity作为身份标识
        logger.info("sending client identity");
        out.println(clientInputs.getClientIdentity().toHex());

        // 获取服务器生成的上下文context
        Data context = Data.fromHex(in.readLine());
        logger.info("context received");

        // 生成ke1并发送
        logger.info("sending ke1");
        KE1 ke1 = clientInit(clientState, clientInputs.getPassword());
        out.println(ke1.toHex());

        // 获取ke2
        KE2 ke2 = KE2.fromHex(in.readLine());
        logger.info("ke2 received");


        // 生成ke3并发送
        logger.info("sending ke3");
        ClientFinishResult clientFinishResult = Opaque.clientFinish(clientState, clientInputs.getClientIdentity(), clientInputs.getServerIdentity(), ke2, MHF.IDENTITY, null, context);
        KE3 ke3 = clientFinishResult.getKE3();
        out.println(ke3.toHex());

        // 输出
        Data sessionKey = clientFinishResult.getSessionKey();
        Data exportKey = clientFinishResult.getExportKey();

        logger.info("sending dowmload option");
        out.println("DOWNLOAD");

        Data enRecord = Data.fromHex(in.readLine());
        RegistrationRecord record = new RegistrationRecord(new Data(opaqueService.decrypt(enRecord.toBytes(), sessionKey.toBytes())));


        byte[] file = Data.fromHex(in.readLine()).toBytes();
        logger.info("file received");

        byte[] decryptedFile = opaqueService.decrypt(file, exportKey.toBytes());

        // byte[] decryptedFile = opaqueService.decrypt(file, record.getMaskingKey().toBytes());

        out.close();
        in.close();

        return decryptedFile;
    }

    public static void startLoginAndDelete(OpaqueUser opaqueUser) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // 创建Socket对象，并连接到服务器
        Socket socket = new Socket(serverAddress, serverPort);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        OpaqueService opaqueService = new OpaqueService();

        // 发送登录请求，开始登录
        logger.info("start log in");
        out.println(LOGIN);

        ClientState clientState = new ClientState();
        ClientInputs clientInputs = new ClientInputs(
                new Data(opaqueUser.getServerIdentity()),
                new Data(opaqueUser.getClientIdentity()),
                new Data(opaqueUser.getPassword())
        );

        // 发送client identity作为身份标识
        logger.info("sending client identity");
        out.println(clientInputs.getClientIdentity().toHex());

        // 获取服务器生成的上下文context
        Data context = Data.fromHex(in.readLine());
        logger.info("context received");

        // 生成ke1并发送
        logger.info("sending ke1");
        KE1 ke1 = clientInit(clientState, clientInputs.getPassword());
        out.println(ke1.toHex());

        // 获取ke2
        KE2 ke2 = KE2.fromHex(in.readLine());
        logger.info("ke2 received");

        // 生成ke3并发送
        logger.info("sending ke3");
        ClientFinishResult clientFinishResult = Opaque.clientFinish(clientState, clientInputs.getClientIdentity(), clientInputs.getServerIdentity(), ke2, MHF.IDENTITY, null, context);
        KE3 ke3 = clientFinishResult.getKE3();
        out.println(ke3.toHex());

        // 输出
        Data sessionKey = clientFinishResult.getSessionKey();
        Data exportKey = clientFinishResult.getExportKey();

        logger.info("sending delete option");
        out.println("DELETE");

        out.close();
        in.close();
    }

    public static void main(String[] args) throws IOException {
        // startRegister();
    }

    public static class result {
        Data exportKey;
        byte[] file;

        public result(Data exportKey, byte[] file) {
            this.exportKey = exportKey;
            this.file = file;
        }

        public Data getExportKey() {
            return exportKey;
        }

        public byte[] getFile() {
            return file;
        }
    }
}


