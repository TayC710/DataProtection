package web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FileService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "fileList", value = "/fileList")
public class FileListServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    private final FileService fileService = new FileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String queryString = req.getQueryString();
        HttpSession session = req.getSession();
        resp.setCharacterEncoding("UTF-8");

        if (queryString != null && queryString.equals("display=1")) {
            // 请求查询文件列表
            if (!isLogin(session)) {
                logger.info("access denied");
                resp.sendError(10003, "access denied");
            }

            String username = (String) session.getAttribute("username");
            String files = fileService.getFileNames(username);
            logger.info("files: " + files);

            PrintWriter out = resp.getWriter();
            out.write(files);
            out.close();
        } else {
            // 请求跳转到list,html
            if (!isLogin(session)) {
                logger.info("access denied");
                resp.sendError(10003, "access denied");
            }

            logger.info("require list.html");
            req.getRequestDispatcher("list.html").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    static boolean isLogin(HttpSession session) {
        if (session.getAttribute("isLogin") == null) {
            return false;
        } else {
            return (boolean) session.getAttribute("isLogin");
        }
    }
}