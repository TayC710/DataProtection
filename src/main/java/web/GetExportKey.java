package web;

import Opaque.OpaqueClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ssohub.crypto.ecc.Util;
import service.FidoService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

@WebServlet(name = "getExportKey", value = "/getExportKey")
public class GetExportKey extends HttpServlet {
    private final static Logger logger = LoggerFactory.getLogger(GetExportKey.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // HttpSession session = request.getSession();
        // if (!isLogin(session)) {
        //     logger.info("access denied");
        //     response.sendError(10003, "access denied");
        // }
        //
        // String username = (String) session.getAttribute("username");
        // byte[] exportKey = OpaqueClient.startRegister(username);
        //
        // logger.info(Util.bin2hex(exportKey));
        //
        // PrintWriter writer = response.getWriter();
        // writer.write(Util.bin2hex(exportKey));
        // writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    static boolean isLogin(HttpSession session) {
        if (session.getAttribute("isLogin") == null) {
            return false;
        } else {
            return (boolean) session.getAttribute("isLogin");
        }
    }
}
