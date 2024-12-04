package web;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FidoService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@WebServlet(name = "upload", value = "/upload")
public class UploadServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    String name;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart) {
            // 创建文件上传处理对象
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置缓冲区大小
            factory.setSizeThreshold(4096);
            // 设置文件存储位置
            factory.setRepository(new File("E:\\Temp_File"));
            // 创建文件上传对象
            ServletFileUpload upload = new ServletFileUpload(factory);

            // 解析请求内容，获取文件数据
            List<FileItem> items = null;
            try {
                items = upload.parseRequest(req);
            } catch (FileUploadException e) {
                throw new RuntimeException(e);
            }

            for (FileItem item : items) {
                if (!item.isFormField()) {
                    // 处理文件数据
                    String fieldName = item.getFieldName();
                    String fileName = item.getName();
                    String contentType = item.getContentType();
                    long sizeInBytes = item.getSize();
                    InputStream fileContent = item.getInputStream();

                    byte[] bytes = fileContent.readAllBytes();

                    item.getInputStream().close();
                    fileContent.close();

                    logger.info("file name: " + fileName);

                    HttpSession session = req.getSession();

                    FidoService fidoService = new FidoService();
                    // fidoService.updateFilePaths((String) session.getAttribute("username"), fileName);
                    try {
                        fidoService.opaqueRegister((String) session.getAttribute("username"), fileName, bytes);
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
                    // OpaqueClient.startRegister((String) session.getAttribute("username"), bytes);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doGet(req, resp);
    }

}
