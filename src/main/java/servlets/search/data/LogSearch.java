package servlets.search.data;

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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Guardeec on 26.10.16.
 */
@WebServlet (name = "logSearchGetData", urlPatterns = "/logSearchGetData")
public class LogSearch extends HttpServlet {
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

        Integer idFrom, idTo, severity, facility;
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

        Integer pageNumber = Integer.parseInt(req.getParameter("pageNumber"));

        String sort = "id";
        switch (req.getParameter("sort")){
            case "date":{
                sort = "events.date_time";
                break;
            }
            case "time":{
                sort = "date_time";
                break;
            }
            case "facility":{
                sort = "facility.descr";
                break;
            }
            case "severity":{
                sort = "severity.descr";
                break;
            }
            case "host":{
                sort = "events.host";
                break;
            }
            case "message":{
                sort = "events.descr";
                break;
            }
        }

        Boolean sortUp = Boolean.parseBoolean(req.getParameter("sortUp"));

        List<Log> logs = (List<Log>) Spring_DAO.getInstance().getSyslogDataBaseSearchByPage().getLogOnPage(
                idFrom, idTo,
                timeFrom, timeTo,
                facility, severity,
                host, message,
                pageNumber,
                sort, sortUp
        );

        writer.write(new Gson().toJson(logs));
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
