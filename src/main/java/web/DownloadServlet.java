package web;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FidoService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "download", value = "/download")
public class DownloadServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    FidoService fidoService = new FidoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        String userName = (String) session.getAttribute("username");

        req.setCharacterEncoding("UTF-8");

        String filename = req.getParameter("filename");
        String s = new String(filename.getBytes("ISO-8859-1"), "UTF-8");
        logger.info("filename: " + s);

        byte[] file;
        try {
            file = fidoService.opaqueLoginAndGetFile(userName, s);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        // logger.info(jsonObject.toJSONString());


        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.write(file, 0, file.length);

        outputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doGet(req, resp);
    }
}
