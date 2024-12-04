package web;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FidoService;
import util.UserNotRegisterErr;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AuthenticStep1", value = "/AuthenticStep1")
public class AuthenticStep1 extends HttpServlet {
    final private Logger logger = LoggerFactory.getLogger(getClass());
    private FidoService fidoService = new FidoService();
    private String username;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();

        BufferedReader reader = request.getReader();

        String inputData = reader.readLine();
        JSONObject data = JSON.parseObject(inputData);
        username = data.getString("username");

        reader.close();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");

        httpSession.setAttribute("username", username);
        httpSession.setAttribute("fidoServer", fidoService);

        String credentialGetJson = null;
        try {
            credentialGetJson = fidoService.createFidoAuthentication(username);
            logger.info("");
            PrintWriter out = response.getWriter();
            out.write(credentialGetJson);
            out.close();
        } catch (UserNotRegisterErr e) {
            logger.warn(username + " is not registered.");
            response.sendError(10002, username + " is not registered.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
