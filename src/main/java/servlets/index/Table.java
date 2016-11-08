package servlets.index;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Guardeec on 06.09.16.
 */
@WebServlet (name = "indexTable", urlPatterns = "/indexTable")
public class Table extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        RequestDispatcher view = req.getRequestDispatcher("/pages/index/table/table.html");
        view.forward(req, resp);
    }
}
