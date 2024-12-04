package web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FidoService;
import util.ExistUserErr;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@WebServlet("/register")
public class RegisterFidoServlet extends HttpServlet {

    final private Logger logger = LoggerFactory.getLogger(getClass());
    private FidoService fidoService = new FidoService();
    private String username;
    private String inputCode;
    private String correctCode;
    /*
    Err_Code:
    10001 用户已注册
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();

        String inputData = reader.readLine();
        JSONObject data = JSON.parseObject(inputData);
        username = data.getString("username");
        inputCode = data.getString("inputCode");

        reader.close();

        if (!Objects.equals(username, "null") && username != null) {

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");

            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("username", username);
            httpSession.setAttribute("fidoServer", fidoService);

            // 验证码比对
            logger.info("Check code.");
            if (inputCode == null | !inputCode.equals(httpSession.getAttribute("correctVerificationCode"))) {
                logger.warn("VerificationCode Wrong.");
                response.sendError(10004, "VerificationCode Wrong");
                return;
            }

            try {
                // 1. 生成验证挑战
                String credentialCreateJson = fidoService.startRegisterNewUser(username);

                // 2. 将验证挑战返回给浏览器
                PrintWriter out = response.getWriter();
                out.write(credentialCreateJson);
                out.close();
                logger.info(credentialCreateJson);
                logger.info("Success");

            } catch (ExistUserErr e) {
//                System.out.println(e.getMessage());
                response.sendError(10001, username + " already exists");
            }
        } else {
            logger.error("Username is Null");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
