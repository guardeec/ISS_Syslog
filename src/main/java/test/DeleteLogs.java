package test;

import containers.Spring_DAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Guardeec on 03.11.16.
 */
@WebServlet (name="deleteLogs", urlPatterns = "/deleteLogs")
public class DeleteLogs extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Spring_DAO.getInstance().getDeleteAllLogs().deleteLogs();
    }
}
