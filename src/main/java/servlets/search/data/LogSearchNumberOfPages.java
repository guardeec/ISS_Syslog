package servlets.search.data;

import com.google.gson.Gson;
import containers.Spring_DAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Guardeec on 03.11.16.
 */
@WebServlet (name = "LogSearchGetNumberOfPages", urlPatterns = "/logSearchGetNumberOfPages")
public class LogSearchNumberOfPages extends HttpServlet {
    private final SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
    private final SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyyMMddhhmm");


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = resp.getWriter();

        String  message  = req.getParameter("message").isEmpty() ? null : req.getParameter("message"),
                host     = req.getParameter("host").isEmpty()    ? null : req.getParameter("host");
//        String  severity = req.getParameter("severity").isEmpty()? null : req.getParameter("severity"),
//                facility = req.getParameter("facility").isEmpty()? null : req.getParameter("facility");

        Timestamp timeFrom  = getTimeStampFromMessageFrom(req.getParameter("dateFrom"), req.getParameter("timeFrom"));
        Timestamp timeTo    = getTimeStampFromMessageTo(req.getParameter("dateTo"), req.getParameter("timeTo"));

        Integer idFrom, idTo, facility, severity;
        if (org.apache.commons.lang.math.NumberUtils.isNumber(req.getParameter("idFrom"))){
            idFrom= Integer.parseInt(req.getParameter("idFrom"));
        }else {
            idFrom=null;
        }
        if (org.apache.commons.lang.math.NumberUtils.isNumber(req.getParameter("idTo"))){
            idTo= Integer.parseInt(req.getParameter("idTo"));
        }else {
            idTo=null;
        }
        if (org.apache.commons.lang.math.NumberUtils.isNumber(req.getParameter("severity"))){
            severity= Integer.parseInt(req.getParameter("severity"));
        }else {
            severity=null;
        }
        if (org.apache.commons.lang.math.NumberUtils.isNumber(req.getParameter("facility"))){
            facility= Integer.parseInt(req.getParameter("facility"));
        }else {
            facility=null;
        }

        Integer[] number = Spring_DAO.getInstance().getSyslogDataBaseSearchByPage().numberOfPages(
                idFrom, idTo,
                timeFrom, timeTo,
                facility, severity,
                host, message
        );

        writer.write(new Gson().toJson(number));
        writer.close();
    }

    private Timestamp getTimeStampFromMessageFrom(String date, String time){
        if (date==null || date.isEmpty()){
            date = formatDate.format(new java.util.Date());
        }else {
            date = date.replaceAll("-","");
        }
        if (time==null || time.isEmpty()){
            time = "0000";
        }else {
            time=time.replaceAll(":","");
        }
        try {
            return new Timestamp(formatDateTime.parse(date+time).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    private Timestamp getTimeStampFromMessageTo(String date, String time){
        if (date==null || date.isEmpty()){
            date = formatDate.format(new java.util.Date());
        }else {
            date = date.replaceAll("-","");
        }
        if (time==null || time.isEmpty()){
            time = "2359";
        }else {
            time=time.replaceAll(":","");
        }
        try {
            return new Timestamp(formatDateTime.parse(date+time).getTime());
        } catch (ParseException e) {
            return null;
        }
    }
}
