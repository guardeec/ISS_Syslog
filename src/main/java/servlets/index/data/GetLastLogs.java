package servlets.index.data;

import com.google.gson.Gson;
import containers.Log;
import containers.Spring_DAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Guardeec on 06.09.16.
 */
@WebServlet(name = "getLastLogs", urlPatterns = "/getLastLogs")
public class GetLastLogs extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = resp.getWriter();
        List<Log> logs = Spring_DAO.getInstance().getSyslogDataBaseDAO().getLastLogs(Integer.parseInt(req.getParameter("limit")),null, null, null, null, null, null, null, null, null, null, null, null);
        writer.write(new Gson().toJson(logs));
        writer.close();
    }

    /**
     * Created by Guardeec on 31.08.16.
     */
    @WebServlet (name = "getLogs", urlPatterns = "/getLogs")
    public static class GetLogs extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setHeader("Access-Control-Allow-Origin", "*");
            PrintWriter writer = resp.getWriter();
            List<Log> logs = Spring_DAO.getInstance().getSyslogDataBaseDAO().getLog(null, null, null, null, null, null, null, null, null, null, null, null);
            writer.write(new Gson().toJson(logs));
            writer.close();
        }
    }
}
