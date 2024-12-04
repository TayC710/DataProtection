package service;

import mapper.FidoUserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.FidoUser;
import util.SqlSessionFactoryUtils;

import java.util.Arrays;

public class FileService {
    final Logger logger = LoggerFactory.getLogger(getClass());
    private final SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();

    public String getFileNames(String username) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        FidoUserMapper fidoUserMapper = sqlSession.getMapper(FidoUserMapper.class);

        logger.info("get FIDO user by name");
        FidoUser user = fidoUserMapper.getUserByName(username);

        return user.getFiles();
    }

    public void uploadFile(byte[] file) {
        logger.info("file received");
        logger.info("file bytes:" + Arrays.toString(file));

    }
}
