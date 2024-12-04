package service;

import Opaque.OpaqueClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import mapper.CredentialMapper;
import mapper.FidoUserMapper;
import mapper.OpaqueUserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.Credential;
import pojo.FidoUser;
import pojo.OpaqueUser;
import util.CredentialRepositoryImpl;
import util.ExistUserErr;
import util.SqlSessionFactoryUtils;
import util.UserNotRegisterErr;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

public class FidoService {
    final Logger logger = LoggerFactory.getLogger(getClass());
    private final SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();
    private final String fidoServiceID = "localhost";
    private final String fidoServiceName = "FidoServiceID@test.com";
    private RelyingPartyIdentity relyingPartyIdentity;
    private RelyingParty rp;
    private Random random;
    private PublicKeyCredentialCreationOptions request;
    private AssertionRequest authenticationResult;
    private byte[] userHandle;


    public FidoService() {
    }

    /**
     * 使用时间：认证
     * 作用：生成认证参数credentialGetJson
     *
     * @param username 用户名
     * @return 生成认证参数credentialGetJson，该参数需要返回给客户端
     */
    public String createFidoAuthentication(String username) throws JsonProcessingException, UserNotRegisterErr {
        logger.info("create relyingPartyIdentity");
        // Set this to a parent domain that covers all subdomains where users' credentials should be valid
        relyingPartyIdentity = RelyingPartyIdentity.builder().id(fidoServiceID).name(fidoServiceName).build();


        logger.info("create relyingParty");
        rp = RelyingParty.builder().identity(relyingPartyIdentity).credentialRepository(new CredentialRepositoryImpl()).allowOriginPort(true).build();

        logger.info("select FIDO user");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        FidoUser fidoUser = fidoUserMapper.getUserByName(username);
        if (fidoUser == null) {
            throw new UserNotRegisterErr(username);
        }

        logger.info("start assertion");
        authenticationResult = rp.startAssertion(StartAssertionOptions.builder().username(username).userHandle(new ByteArray(fidoUser.getUserHandle())).build());    // Or .userHandle(ByteArray) if preferred

        String credentialGetJson = authenticationResult.toCredentialsGetJson();

        logger.info("done");
        // Send to client
        return credentialGetJson;
    }

    // public void updateFilePaths(String userName, String filePath) {
    //     logger.info("updating user " + userName + "'s files");
    //     SqlSession sqlSession = sqlSessionFactory.openSession();
    //     FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);
    //
    //     FidoUser fidoUser = fidoUserMapper.getUserByName(userName);
    //
    //     String files = fidoUser.getFiles() + filePath + ",";
    //     logger.info("fido user's files " + files);
    //
    //     fidoUserMapper.updateFilesByUserName(userName, files);
    //
    //     sqlSession.commit();
    //     sqlSession.close();
    //     logger.info("done");
    // }

    public void opaqueRegister(String fidoUserName, String filename, byte[] file) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        logger.info("updating user " + fidoUserName + "'s files");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        FidoUser fidoUser = fidoUserMapper.getUserByName(fidoUserName);

        String files = fidoUser.getFiles() + filename + ",";
        logger.info("fido user's files " + files);

        fidoUserMapper.updateFilesByUserName(fidoUserName, files);

        sqlSession.commit();
        sqlSession.close();
        logger.info("done");

        // logger.info("get fido user by name " + fidoUserName);
        // SqlSession sqlSession = sqlSessionFactory.openSession();
        // FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);
        //
        // FidoUser fidoUser = fidoUserMapper.getUserByName(fidoUserName);
        //
        // String[] files = fidoUser.getFiles().split(",");
        // logger.info("files: " + Arrays.toString(files));
        // String[] opaqueUsers = fidoUser.getOpaqueUsers().split(",");
        // logger.info("opaque users: " + Arrays.toString(opaqueUsers));
        //
        // int index = 0;
        // for (int i = 0; i < files.length; i++) {
        //     if (files[i].equals(filename)) {
        //         index = Integer.parseInt(opaqueUsers[i]);
        //         logger.info(files[i] + ": " + opaqueUsers[i]);
        //         break;
        //     }
        // }
        //
        // sqlSession.close();
        // logger.info("get opaque user by userId " + index);
        // sqlSession = sqlSessionFactory.openSession();
        // OpaqueUserMapper opaqueUserMapper = sqlSession.getMapper(OpaqueUserMapper.class);
        //
        // OpaqueUser opaqueUser = opaqueUserMapper.selectOpaqueUserByUserId(index);
        // logger.info(opaqueUser.toString());

        OpaqueClient.startRegister(fidoUserName, file);
    }

    public byte[] opaqueLoginAndGetFile(String fidoUserName, String filename) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        logger.info("get fido user by name " + fidoUserName);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        FidoUser fidoUser = fidoUserMapper.getUserByName(fidoUserName);

        // filename = filename.replace(" ", "+");

        String[] files = fidoUser.getFiles().split(",");
        logger.info("files: " + Arrays.toString(files));
        String[] opaqueUsers = fidoUser.getOpaqueUsers().split(",");
        logger.info("opaque users: " + Arrays.toString(opaqueUsers));

        int index = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].equals(filename)) {
                index = Integer.parseInt(opaqueUsers[i]);
                logger.info(files[i] + ": " + opaqueUsers[i]);
                break;
            }
        }

        sqlSession.close();
        logger.info("get opaque user by userId " + index);
        sqlSession = sqlSessionFactory.openSession();
        OpaqueUserMapper opaqueUserMapper = sqlSession.getMapper(OpaqueUserMapper.class);

        OpaqueUser opaqueUser = opaqueUserMapper.selectOpaqueUserByUserId(index);
        logger.info(opaqueUser.toString());

        return OpaqueClient.startLoginAndDownload(opaqueUser);
    }


    /**
     * 注册Step1
     * 注册新用户
     * 实例化RelyingParty
     * RelyingParty类是该库的主要入口点。可以使用它的生成器方法来实例化它，并将CredentialRepositoryImpl()实现
     *
     * @return 返回一个JSON字符串发送给客户端
     */
    public String startRegisterNewUser(String userName) throws JsonProcessingException, ExistUserErr {
        random = new Random();

//        实例化RelyingParty
        logger.info("create relying party identity");
        // Set this to a parent domain that covers all subdomains where users' credentials should be valid
        relyingPartyIdentity = RelyingPartyIdentity.builder().id(fidoServiceID).name(fidoServiceName).build();

        logger.info("create relying party");
        rp = RelyingParty.builder().identity(relyingPartyIdentity).credentialRepository(new CredentialRepositoryImpl()).allowOriginSubdomain(true).allowOriginPort(true).build();

        logger.info("start registration");
        request = rp.startRegistration(StartRegistrationOptions.builder().user(findExistingUser(userName).orElseGet(() -> {
            userHandle = new byte[64];
            random.nextBytes(userHandle);
            return UserIdentity.builder().name(userName).displayName(userName).id(new ByteArray(userHandle)).build();
        })).extensions(RegistrationExtensionInputs.builder().uvm().build()).build());

        // request.getExtensions();

        String credentialCreateJson = request.toCredentialsCreateJson();
        logger.info("credentialCreateJson: " + credentialCreateJson);
        return credentialCreateJson;
    }

    /**
     * 注册Step2
     * 接受客户端产生的公钥凭证并解析，存储在本地
     *
     * @param publicKeyCredentialJson 客户端发送的公钥，为JSON字符串格式。publicKeyCredential from client.
     */
    public void responseRegister(String publicKeyCredentialJson, String username) throws IOException {
        logger.info("parse registration response");
        PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc = PublicKeyCredential.parseRegistrationResponseJson(publicKeyCredentialJson);
        logger.debug("request challenge: " + Arrays.toString(request.getChallenge().getBytes()));
        logger.debug("response challenge: " + Arrays.toString(pkc.getResponse().getClientData().getChallenge().getBytes()));
        try {
            logger.info("finish registration");
            // The PublicKeyCredentialCreationOptions from startRegistration above
            // NOTE: Must be stored in server memory or otherwise protected against tampering
            RegistrationResult registrationResult = rp.finishRegistration(FinishRegistrationOptions.builder().request(request).response(pkc).build());

            logger.info("store credential");
            storeCredential(             // Some database access method of your own design
                    username,                   // Username or other appropriate user identifier
                    registrationResult.getKeyId(),         // Credential ID and transports for allowCredentials
                    registrationResult.getPublicKeyCose(), // Public key for verifying authentication signatures
                    registrationResult.getSignatureCount(),   // Initial signature counter value
                    pkc.getResponse().getAttestationObject(), // Store attestation object for future reference
                    pkc.getResponse().getClientDataJSON(),    // Store client data for re-verifying signature if needed
                    userHandle// 生成uuid
            );
        } catch (RegistrationFailedException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 根据用户名查找存在的用户
     *
     * @return Optional<UserIdentity> 由FidoUser转成的UserIdentity返回
     */
    protected Optional<UserIdentity> findExistingUser(String username) throws ExistUserErr {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        // 通过用户名查询用户
        FidoUser user = fidoUserMapper.getUserByName(username);
        logger.info("user: " + user);

        // 结束session
        sqlSession.close();
        if (user != null) {
            throw new ExistUserErr(username);
        }
//        return user != null ? Optional.of(UserIdentity.builder().name(user.getUserName()).displayName(user.getUserName()).id(new ByteArray(user.getUserHandle())).build()) : Optional.empty();
        return Optional.empty();
    }

    /**
     * 使用时间：认证
     *
     * @param publicKeyCredentialJson 来自客户端的publicKeyCredential
     */
    public void responseAuthentication(String publicKeyCredentialJson) throws IOException {
        logger.info("generating pkc");
        PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc = PublicKeyCredential.parseAssertionResponseJson(publicKeyCredentialJson);


        try {
            logger.info("finishing assertion");
            // The PublicKeyCredentialRequestOptions from startAssertion above
            AssertionResult result = rp.finishAssertion(FinishAssertionOptions.builder().request(authenticationResult).response(pkc).build());

            logger.info("checking result");
            if (result.isSuccess()) {
                updateCredential(              // Some database access method of your own design
                        result.getUsername(),        // Query by username or other appropriate user identifier
                        result.getCredentialId(),    // Query by credential ID of the credential used
                        result.getSignatureCount(),     // Set new signature counter value
                        Clock.systemUTC().instant()  // Set time of last use (now)
                );
                logger.info("done");
            }
        } catch (AssertionFailedException e) {
            logger.error("authentication failed");
            e.printStackTrace();
        }
    }

    /**
     * 使用时间：注册
     * 凭证存入数据库
     *
     * @param username          用户名
     * @param keyId             credentialId 凭证ID
     * @param publicKeyCose     publicKeyCose
     * @param signatureCount    signatureCount
     * @param attestationObject attestationObject
     * @param clientDataJSON    clientDataJSON
     */
    protected void storeCredential(String username, PublicKeyCredentialDescriptor keyId, ByteArray publicKeyCose, long signatureCount, ByteArray attestationObject, ByteArray clientDataJSON, byte[] userHandle) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);
        CredentialMapper credentialMapper = sqlSession.getMapper(CredentialMapper.class);

        // 存储用户表
        logger.info("insert new credential");
        FidoUser fidoUser = new FidoUser();
        fidoUser.setUserName(username);
        fidoUser.setUserHandle(userHandle);
        fidoUserMapper.insertNewUser(fidoUser);
        FidoUser userByName = fidoUserMapper.getUserByName(username);

        Credential credential = new Credential(keyId.getId().getBytes(), userByName.getUserId(), userHandle, publicKeyCose.getBytes(), (int) signatureCount);

        // 存储凭证
        credentialMapper.insertNewCredential(credential);

        try {
            sqlSession.commit();
            logger.info("done");
        } catch (Exception e) {
            logger.error(e.toString());
        }
        sqlSession.close();
    }


    /**
     * 通过username判断用户是否注册
     *
     * @param username 用户名
     * @return boolean 若用户名已被注册则返回true，用户名未被注册则返回false
     */
    public boolean isRegistered(String username) {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        // 通过用户名查询用户
        FidoUser user = fidoUserMapper.getUserByName(username);
        logger.info("user: " + user);

        // 结束session
        sqlSession.close();

        return (user == null);
    }

    /**
     * 注册Fido用户
     *
     * @param username 用户名
     */
    public void RegisterFidoUser(String username, byte[] userHandle) {
        // 判断用户名是否已经注册过
        if (isRegistered(username)) {
            logger.info("Username" + username + "is already in use");
            return;
        }


        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        // 新建FidoUser对象并注册
        FidoUser fidoUser = new FidoUser();
        fidoUser.setUserName(username);
        fidoUser.setUserHandle(userHandle);

        fidoUserMapper.insertNewUser(fidoUser);
        logger.info("FIDO user: " + fidoUser);

        // 提交事务
        try {
            sqlSession.commit();
            logger.info("done");
        } catch (Exception e) {
            logger.error(e.toString());
        }

        // 结束session
        sqlSession.close();
    }

    /**
     * 通过用户名获取对应的凭证列表
     *
     * @param username 用户名
     * @return 凭证列表
     */
    public List<byte[]> getCredentialIdByName(String username) {
        // 判断用户名是否存在
        if (isRegistered(username)) {
            logger.info("Username " + username + " doesn't exist");
            return null;
        }

        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CredentialMapper credentialMapper = sqlSession.getMapper(CredentialMapper.class);

        // 查询用户名对应的凭证对象
        List<Credential> credentials = credentialMapper.getCredentialsByUserName(username);

        // 获取对应的凭证
        List<byte[]> credentialIds = new ArrayList<>();
        for (Credential credential : credentials) {
            logger.info("credential: " + credential.toString());
            credentialIds.add(credential.getCredentialId());
        }

        // 结束session
        sqlSession.close();

        return credentialIds;
    }

    public List<String> getFileList(String username) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        FidoUser fidoUser = fidoUserMapper.getUserByName(username);

        String fileList = fidoUser.getFiles();

        sqlSession.close();
        return Arrays.asList(fileList.split(","));
    }

    public List<String> getOpaqueUserList(String username) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        FidoUser fidoUser = fidoUserMapper.getUserByName(username);

        String opaqueUserList = fidoUser.getOpaqueUsers();
        sqlSession.close();
        return Arrays.asList(opaqueUserList.split(","));
    }

    /**
     * 通过用户名获取对应的用户handle
     *
     * @param username 用户名
     * @return 用户handle
     */
    public byte[] getUserHandleByUserName(String username) {
        // 判断用户名是否存在
        if (isRegistered(username)) {
            logger.info("Username " + username + "doesn't exist");
            return null;
        }

        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        // 查询用户名对应的FidoUser对象
        FidoUser user = fidoUserMapper.getUserByName(username);
        logger.info("user: " + user);

        // 结束session
        sqlSession.close();

        return user.getUserHandle();
    }

    /**
     * 持久化存储认证信息
     */
    protected void updateCredential(String username, ByteArray credentialId, long signatureCount, Instant instant) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);
        CredentialMapper credentialMapper = sqlSession.getMapper(CredentialMapper.class);

        FidoUser fidoUser = fidoUserMapper.getUserByName(username);

        credentialMapper.updateAuthentication((int) signatureCount, fidoUser.getUserId(), credentialId.getBytes());

        try {
            sqlSession.commit();
            logger.info("done");
        } catch (Exception e) {
            logger.error(e.toString());
        }

        sqlSession.close();
    }

    /**
     * 通过用户handle获取对应的用户名
     *
     * @param userHandle 用户的handle
     * @return 用户名
     */
    public String getUserNameByUserHandle(byte[] userHandle) {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        // 查询handle对应的FidoUser对象client_identity
        FidoUser user = fidoUserMapper.getUserByHandle(userHandle);
        logger.info("user: " + user.toString());

        // 结束session
        sqlSession.close();

        return user.getUserName();
    }

    /**
     * 通过凭证id和用户handle得到对应唯一的凭证
     *
     * @param credentialId 凭证id
     * @param userHandle   用户handle
     * @return 唯一凭证
     */
    public Credential getCredentialByCredentialIdAndUserHandle(byte[] credentialId, byte[] userHandle) {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CredentialMapper credentialMapper = sqlSession.getMapper(CredentialMapper.class);

        // 查询对应的凭证
        Credential credential = credentialMapper.getCredentialByCredentialIdAndUserHandle(credentialId, userHandle);

        // 结束session
        sqlSession.close();

        return credential;
    }

    /**
     * 获取凭证id对应的所有凭证
     *
     * @param credentialId 凭证id
     * @return 凭证id对应的凭证列表
     */
    public List<Credential> getCredentialsByCredentialId(byte[] credentialId) {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CredentialMapper credentialMapper = sqlSession.getMapper(CredentialMapper.class);

        // 查询凭证id对应的所有凭证
        List<Credential> credentialList = credentialMapper.getCredentialByCredentialId(credentialId);

        // 结束session
        sqlSession.close();

        return credentialList;
    }

    public void deleteFileByPath(OpaqueUser opaqueUser) throws NoSuchPaddingException, IllegalBlockSizeException, IOException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        OpaqueClient.startLoginAndDelete(opaqueUser);
    }

    public void updateDBAfterDelete(String userName, List<String> files, List<String> opaqueUsers) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);
        fidoUserMapper.updateFilesByUserName(userName, list2String(files));
        fidoUserMapper.updateOpaqueUsersByUserName(userName, list2String(opaqueUsers));
        sqlSession.commit();
        sqlSession.close();
    }

    private String list2String(List<String> in) {
        String out = "";
        for (String s : in) {
            out = s + ",";
        }
        logger.info("format String: " + out);
        return out;
    }
}
