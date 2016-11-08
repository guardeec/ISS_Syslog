package servlets.search;

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
@WebServlet (name = "searchLogs", urlPatterns = "/searchLogs")
public class SearchLogsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        RequestDispatcher view = req.getRequestDispatcher("/pages/search/searchLogs.html");
        view.forward(req, resp);
    }
}
