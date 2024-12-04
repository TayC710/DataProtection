package service;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import mapper.FidoUserMapper;
import mapper.FilesMapper;
import mapper.OpaqueKeysMapper;
import mapper.OpaqueUserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssohub.crypto.ecc.Data;
import org.ssohub.crypto.ecc.opaque.*;
import org.ssohub.crypto.ecc.ristretto255.R255Scalar;
import pojo.FidoUser;
import pojo.Files;
import pojo.OpaqueKeys;
import pojo.OpaqueUser;
import util.FileUtils;
import util.SqlSessionFactoryUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.ssohub.crypto.ecc.Util.randomData;

public class OpaqueService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();

    public OpaqueService() {

    }

    /**
     * 在OPAQUE服务器初始化时设置对应的公私钥对、OPRF种子两个参数
     *
     * @return 以Map形式将两个参数返回
     */
    public Map<String, Object> setServerKeys() {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OpaqueKeysMapper opaqueKeysMapper = sqlSession.getMapper(OpaqueKeysMapper.class);

        // 查询opaque参数
        OpaqueKeys opaqueKeys = opaqueKeysMapper.getOpaqueKeys();
        logger.info("opaqueKeys: " + opaqueKeys);

        OpaqueKeys tempOpaqueKeys = new OpaqueKeys();

        // 若不存在，则新建参数并插入数据库
        if (opaqueKeys == null) {
            // 生成公私钥对
            GenerateAuthKeyPairResult tempKeyPair = Opaque.generateAuthKeyPair();
            // 生成OPRF随机种子
            Data oprfSeed = randomData(64);

            tempOpaqueKeys.setServerPublicKey(tempKeyPair.getPublicKey().toBytes());
            tempOpaqueKeys.setServerPrivateKey(tempKeyPair.getPrivateKey().toBytes());
            tempOpaqueKeys.setOprfSeed(oprfSeed.toBytes());

            opaqueKeysMapper.insertOpaqueKeys(tempOpaqueKeys.getServerPublicKey(), tempOpaqueKeys.getServerPrivateKey(), tempOpaqueKeys.getOprfSeed());
            sqlSession.commit();
        } else {
            tempOpaqueKeys = opaqueKeys;
        }

        sqlSession.close();

        // 构造返回结果
        GenerateAuthKeyPairResult keyPair = new GenerateAuthKeyPairResult(new OpaqueSk(new Data(tempOpaqueKeys.getServerPrivateKey())), new OpaquePk(new Data(tempOpaqueKeys.getServerPublicKey())));

        Map<String, Object> result = new HashMap<>();
        result.put("GenerateAuthKeyPairResult", keyPair);
        result.put("Data", new Data(tempOpaqueKeys.getOprfSeed()));

        logger.info("result: " + result);
        logger.info("done");

        return result;
    }

    /**
     * 添加新文件并存储对应的record等信息
     */
    public void storeServerInfo(ServerInputs serverInputs, RegistrationRecord record) {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FilesMapper fileMapper = sqlSession.getMapper(FilesMapper.class);

        logger.info("inserting new record");
        logger.info("client identity: " + Arrays.toString(serverInputs.getClientIdentity().toBytes()));
        Files files = new Files(serverInputs.getCredentialIdentifier().toBytes(), serverInputs.getClientIdentity().toBytes(), record.toBytes());

        fileMapper.insertFileRecord(files);

        sqlSession.commit();
        sqlSession.close();

        logger.info("done");
    }

    /**
     * 注册新的OPAQUE用户
     */
    public void storeClientInfo(ClientInputs clientInputs, R255Scalar blind, OpaqueSeed nonce, GenerateAuthKeyPairResult keyPair, String fidoUserName) {
        byte[] clientIdentity = clientInputs.getClientIdentity().toBytes();
        byte[] serverIdentity = clientInputs.getServerIdentity().toBytes();
        byte[] password = clientInputs.getPassword().toBytes();

        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OpaqueUserMapper opaqueUserMapper = sqlSession.getMapper(OpaqueUserMapper.class);

        logger.info("inserting new opaque user");

        OpaqueUser user = new OpaqueUser(clientIdentity, password, serverIdentity, blind.toBytes(), nonce.toBytes(), keyPair.getPublicKey().toBytes(), keyPair.getPrivateKey().toBytes());

        opaqueUserMapper.insertNewUser(user);

        sqlSession.commit();

        int id = opaqueUserMapper.selectOpaqueUserByClientIdentity(user.getClientIdentity()).getUserId();
        sqlSession.close();

        sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        logger.info("get FIDO user by name: " + fidoUserName);
        FidoUser fidoUser = fidoUserMapper.getUserByName(fidoUserName);
        String opaqueUsers = fidoUser.getOpaqueUsers() + id + ",";
        logger.info("FIDO user " + fidoUserName + "'s OPAQUE users: " + opaqueUsers);

        logger.info("update OPAQUE users");
        fidoUserMapper.updateOpaqueUsersByUserName(fidoUserName, opaqueUsers);

        sqlSession.commit();
        sqlSession.close();

        logger.info("done");
    }

    public OpaqueUser getOpaqueUser(int userId) {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OpaqueUserMapper opaqueUserMapper = sqlSession.getMapper(OpaqueUserMapper.class);

        logger.info("select opaque user by user id");
        OpaqueUser opaqueUser = opaqueUserMapper.selectOpaqueUserByUserId(userId);

        logger.info(opaqueUser.toString());
        sqlSession.close();

        return opaqueUser;
    }

    public Files getFiles(Data clientIdentity) {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FilesMapper filesMapper = sqlSession.getMapper(FilesMapper.class);

        logger.info("select files by client identity");
        Files files = filesMapper.selectFileByClientIdentity(clientIdentity.toBytes());

        logger.info(files.toString());
        sqlSession.close();

        return files;
    }

    public OpaqueKeys getOpaqueKeys() {
        // 通过连接池获取session，并得到对应mapper
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OpaqueKeysMapper opaqueKeysMapper = sqlSession.getMapper(OpaqueKeysMapper.class);

        logger.info("select opaque keys");
        OpaqueKeys opaqueKeys = opaqueKeysMapper.getOpaqueKeys();

        logger.info(opaqueKeys.toString());
        sqlSession.close();

        return opaqueKeys;
    }

    public void storeFile(byte[] data, Data credentialIdentifier) throws IOException {
        // 根据时间戳生成文件保存路径
        String filepath = "E://files//" + System.currentTimeMillis();
        logger.info("file path: " + filepath);

        // 更新数据库
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FilesMapper filesMapper = sqlSession.getMapper(FilesMapper.class);

        logger.info("update path by credential identifier");
        filesMapper.updatePathByCredentialIdentifier(filepath, credentialIdentifier.toBytes());

        sqlSession.commit();
        sqlSession.close();

        // 在本地保存文件
        logger.info("storing file");
        FileUtils.write(data, filepath);
        logger.info("done");
    }

    public byte[] getLocalFile(Data clientIdentity) throws IOException {
        // 获取文件路径
        OpaqueService opaqueService = new OpaqueService();
        Files files = opaqueService.getFiles(clientIdentity);
        logger.info(files.toString());

        // 读取文件
        logger.info("reading file into buffer " + files.getPath());
        return FileUtils.read(files.getPath());
    }

    public Data encrypt(byte[] file, byte[] key) {
        logger.info("encrypting file");

        byte[] encKey = new byte[16];
        System.arraycopy(key, 0, encKey, 0, 16);

        SymmetricCrypto sm4 = SmUtil.sm4(encKey);
        byte[] encrypt = sm4.encrypt(file);
        Data result = new Data(encrypt);

        logger.info("done");
        return result;
    }

    public byte[] decrypt(byte[] file, byte[] key) {
        logger.info("decrypting file");

        byte[] encKey = new byte[16];
        System.arraycopy(key, 0, encKey, 0, 16);

        SymmetricCrypto sm4 = SmUtil.sm4(encKey);
        byte[] decrypt = sm4.decrypt(file);

        logger.info("done");
        return decrypt;
    }

    /*
     public Data encrypt(byte[] file, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
         logger.info("encrypting file");
         byte[] encKey = new byte[16];
         System.arraycopy(key, 0, encKey, 0, 16);
         SecretKeySpec keySpec = new SecretKeySpec(encKey, "AES");
         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
         cipher.init(Cipher.ENCRYPT_MODE, keySpec);
         Data result = new Data(cipher.doFinal(file));
         logger.info("done");
         return result;
     }
     public byte[] decrypt(byte[] file, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
         logger.info("decrypting file");
         byte[] encKey = new byte[16];
         System.arraycopy(key, 0, encKey, 0, 16);
         SecretKeySpec keySpec = new SecretKeySpec(encKey, "AES");
         Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
         cipher.init(Cipher.DECRYPT_MODE, keySpec);
         logger.info("done");
         return cipher.doFinal(file);
     }
    */

}
