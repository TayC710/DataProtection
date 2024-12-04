package web;

import mapper.FidoUserMapper;
import mapper.FilesMapper;
import mapper.OpaqueUserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.Files;
import pojo.OpaqueUser;
import service.FidoService;
import service.FileService;
import util.SqlSessionFactoryUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "delete", value = "/delete")
public class DeleteServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    FidoService fidoService = new FidoService();
    private final FileService fileService = new FileService();
    private final SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtils.getSqlSessionFactory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");

        request.setCharacterEncoding("UTF-8");

        String s = request.getParameter("filename");
        String filename = new String(s.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        logger.info("delete file: " + filename);
        List<String> fileList = new ArrayList<>(fidoService.getFileList(userName));
        List<String> opaqueUserList = new ArrayList<>(fidoService.getOpaqueUserList(userName));
        String opaqueUserID;

        if (fileList.contains(filename)) {
            logger.info("fileList: " + fileList + " filename.indexOf(filename) is " + filename.indexOf(filename));
            int index = filename.indexOf(filename);
            fileList.remove(index);
            opaqueUserID = opaqueUserList.get(index);
            opaqueUserList.remove(index);
            logger.info("get OpaqueUserID " + opaqueUserID + " by file:" + filename);

            SqlSession sqlSession = sqlSessionFactory.openSession();

            OpaqueUserMapper opaqueUserMapper = sqlSession.getMapper(OpaqueUserMapper.class);
            FilesMapper filesMapper = sqlSession.getMapper(FilesMapper.class);

            OpaqueUser opaqueUser = opaqueUserMapper.selectOpaqueUserByUserId(Integer.parseInt(opaqueUserID));
            Files files = filesMapper.selectFileByClientIdentity(opaqueUser.getClientIdentity());
            String path = files.getPath();

            opaqueUserMapper.deleteByUserId(Integer.parseInt(opaqueUserID));
            filesMapper.deleteByPath(path);
            try {
                fidoService.deleteFileByPath(opaqueUser);
                fidoService.updateDBAfterDelete(userName, fileList, opaqueUserList);
                sqlSession.commit();
                logger.info("delete finished");
            } catch (Exception e) {
                e.printStackTrace();
            }
            sqlSession.close();
        } else {
            logger.info(filename + " doesn't exist.");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
