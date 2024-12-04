package web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FidoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "AuthenticStep2", value = "/AuthenticStep2")
public class AuthenticStep2 extends HttpServlet {
    final Logger logger = LoggerFactory.getLogger(getClass());
    private FidoService fidoService;
    private String username;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession httpSession = request.getSession();
        username = (String) httpSession.getAttribute("username");
        fidoService = (FidoService) httpSession.getAttribute("fidoServer");


        BufferedReader reader = request.getReader();
        String publicKeyCredentialJson = reader.readLine();
        logger.info("publicKeyCredentialJson:" + publicKeyCredentialJson);

        fidoService.responseAuthentication(publicKeyCredentialJson);

        reader.close();
        httpSession.setAttribute("isLogin", true);
        logger.info("log in");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
