package Opaque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssohub.crypto.ecc.Data;
import org.ssohub.crypto.ecc.Util;
import org.ssohub.crypto.ecc.opaque.*;
import pojo.Files;
import pojo.OpaqueKeys;
import service.OpaqueService;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

import static org.ssohub.crypto.ecc.opaque.Opaque.*;

public class OpaqueServer {
    public static final int DOWNLOAD = 3;
    final static Logger logger = LoggerFactory.getLogger(OpaqueServer.class);
    private static final int serverPort = 9000; // 服务器端口号
    private static final int REGISTER = 0;
    private static final int LOGIN = 1;
    private static final int UPLOAD = 2;
    private static OpaqueService opaqueService = null;
    private static GenerateAuthKeyPairResult keyPairResult;
    private static Data oprfSeed;

    private static void handleRegister(BufferedReader in, PrintWriter out) throws IOException {
        logger.info("start register");

        // 向客户端发送公钥作为identity
        logger.info("sending server public key as identity");
        Data serverIdentity = Data.fromHex(keyPairResult.getPublicKey().toHex());
        out.println(serverIdentity.toHex());

        // 接收客户端的公钥作为identity
        Data clientIdentity = Data.fromHex(in.readLine());
        logger.info("client identity received");

        // 为注册的用户生成随机的credential identifier
        Data credentialIdentifier = Util.randomData(10);

        ServerInputs serverInputs = new ServerInputs(
                keyPairResult.getPrivateKey(),
                keyPairResult.getPublicKey(),
                credentialIdentifier,
                serverIdentity,
                clientIdentity,
                oprfSeed
        );

        // 接收注册请求
        RegistrationRequest request = RegistrationRequest.fromHex(in.readLine());
        logger.info("client request received");

        // 生成注册响应
        RegistrationResponse registrationResponse = createRegistrationResponse(
                request,
                serverInputs.getServerPublicKey(),
                serverInputs.getCredentialIdentifier(),
                serverInputs.getOprfSeed()
        );

        // 发送response
        logger.info("sending registration response");
        out.println(registrationResponse.toHex());

        // 接收注册最后的record
        RegistrationRecord record = RegistrationRecord.fromHex(in.readLine());
        logger.info("registration record received");

        byte[] enc_file = Data.fromHex(in.readLine()).toBytes();
        logger.info("file received");

        opaqueService.storeServerInfo(serverInputs, record);

        logger.info("store file");
        opaqueService.storeFile(enc_file, serverInputs.getCredentialIdentifier());
        logger.info("done");
    }

    private static Data handleLogin(BufferedReader in, PrintWriter out) throws IOException {
        logger.info("start log in");
        ServerState serverState = new ServerState();
        OpaqueService opaqueService = new OpaqueService();

        // 获取客户端client identity
        Data clientIdentity = Data.fromHex(in.readLine());
        logger.info("client identity received");
        logger.info("client identity: " + Arrays.toString(clientIdentity.toBytes()));

        // 生成上下文context并与客户端同步
        logger.info("sending context");
        Data context = Util.randomData(10);
        out.println(context.toHex());

        Files file = opaqueService.getFiles(clientIdentity);
        System.out.println(new RegistrationRecord(new Data(file.getRecord())).getMaskingKey().toHex());
        OpaqueKeys opaqueKeys = opaqueService.getOpaqueKeys();

        keyPairResult = new GenerateAuthKeyPairResult(
                new OpaqueSk(new Data(opaqueKeys.getServerPrivateKey())),
                new OpaquePk(new Data(opaqueKeys.getServerPublicKey()))
        );
        oprfSeed = new Data(opaqueKeys.getOprfSeed());

        ServerInputs serverInputs = new ServerInputs(
                keyPairResult.getPrivateKey(),
                keyPairResult.getPublicKey(),
                new Data(file.getCredentialIdentifier()),
                keyPairResult.getPublicKey().getData(),
                clientIdentity, oprfSeed
        );
        RegistrationRecord record = new RegistrationRecord(new Data(file.getRecord()));

        // 获取客户端的ke1
        KE1 ke1 = KE1.fromHex(in.readLine());
        logger.info("ke1 received");

        // 生成ke2并发送
        logger.info("sending ke2");
        KE2 ke2 = serverInit(
                serverState,
                serverInputs.getServerIdentity(),
                serverInputs.getServerPrivateKey(),
                serverInputs.getServerPublicKey(),
                record,
                serverInputs.getCredentialIdentifier(),
                serverInputs.getOprfSeed(),
                ke1,
                serverInputs.getClientIdentity(),
                context
        );
        out.println(ke2.toHex());

        // 获取ke3
        KE3 ke3 = KE3.fromHex(in.readLine());
        logger.info("ke3 received");

        // 从ke3中生成session key
        ServerFinishResult serverFinishResult = serverFinish(serverState, ke3);
        Data sessionKey = serverFinishResult.getSessionKey();

        String option = in.readLine();
        if (option.equals("DOWNLOAD")) {
            logger.info("downloading file");

            logger.info("sending encrypted record");
            Data enRecord = opaqueService.encrypt(record.toBytes(), sessionKey.toBytes());
            out.println(enRecord.toHex());

            byte[] localFile = opaqueService.getLocalFile(serverInputs.getClientIdentity());

            logger.info("send local file");
            out.println(new Data(localFile).toHex());
        } else {
            logger.info("deleting local file");
            Files files = opaqueService.getFiles(serverInputs.getClientIdentity());
            try {
                File localFile = new File(files.getPath());
                if (localFile.delete()) {
                    logger.info("file deleted");
                }else {
                    logger.info("delete file failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionKey;
    }

    public static void main(String[] args) throws IOException {
        // 创建ServerSocket对象，并监听端口号
        ServerSocket serverSocket = new ServerSocket(serverPort);

        opaqueService = new OpaqueService();

        // 获取公私钥对及OPRF种子
        Map<String, Object> result = opaqueService.setServerKeys();
        keyPairResult = (GenerateAuthKeyPairResult) result.get("GenerateAuthKeyPairResult");
        oprfSeed = (Data) result.get("Data");

        logger.info("successfully set server's key pair & oprf seed.");

        while (true) {
            // 接收客户端的连接请求，并创建Socket对象
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            switch (Integer.parseInt(in.readLine())) {
                case REGISTER -> handleRegister(in, out);
                case LOGIN -> {
                    Data sessionKey = handleLogin(in, out);
                }
            }

            in.close();
            out.close();
        }
    }
}
