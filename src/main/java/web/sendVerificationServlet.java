package web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.VerificateUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "sendVerificationServlet", value = "/getVerification")
public class sendVerificationServlet extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    private String email;
    private String code = null;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        String inputData = reader.readLine();
        JSONObject data = JSON.parseObject(inputData);
        email = data.getString("username");

        try {
            VerificateUtils verificateUtils = new VerificateUtils();
            code = verificateUtils.send(email);
            HttpSession session = request.getSession();
            logger.info("Generate code: "+code);
            session.setAttribute("username", email);
            session.setAttribute("correctVerificationCode",code);
        } catch (EmailException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
