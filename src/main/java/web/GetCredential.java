package web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.FidoService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "getCredential", value = "/getCredential")
public class GetCredential extends HttpServlet {
    private FidoService fidoService;
    private String username;
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        BufferedReader reader = request.getReader();
        String publicKeyCredentialJson = reader.readLine();

        HttpSession httpSession = request.getSession();
        username = (String) httpSession.getAttribute("username");
        fidoService = (FidoService) httpSession.getAttribute("fidoServer");
        logger.info("session's username: " + username);

        logger.info("JSON: " + publicKeyCredentialJson);
        fidoService.responseRegister(publicKeyCredentialJson,username);
        reader.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
