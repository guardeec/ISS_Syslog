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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Guardeec on 06.09.16.
 */
@WebServlet (name = "logsSingleBarViz", urlPatterns = "/logsSingleBarViz")
public class LogsSingleBarViz extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = resp.getWriter();
        List<Log> logs = Spring_DAO.getInstance().getSyslogDataBaseDAO().getLastLogs(500, null, null, null, null, null, null, null, null, null, null, null, null);
        Map<String, Integer> types = new HashMap<>();
        for (Log log : logs){
            if (types.containsKey(log.getSeverity())){
                types.put(log.getSeverity(), types.get(log.getSeverity())+1);
            }else {
                types.put(log.getSeverity(), 1);
            }
        }
        float numberOfLogs = 0;
        for (String key : types.keySet()){
            numberOfLogs+=types.get(key);
        }
        SingleBarVizData singleBarVizData = new SingleBarVizData();
        for (String key : types.keySet()){
            singleBarVizData.addBar(key, ((float)types.get(key))/numberOfLogs);
        }

        writer.write(new Gson().toJson(singleBarVizData));
        writer.close();
    }

    private class SingleBarVizData{
        Map<String, Float> bars;
        public SingleBarVizData() {
            this.bars = new HashMap<>();
            bars.put("info", 0f);
            bars.put("debug", 0f);
            bars.put("alert", 0f);
        }
        public void addBar(String facility, float count){
            if (facility.contains("Info")){
                bars.put("info", count);
            }
            if (facility.contains("Alert")){
                bars.put("alert", count);
            }
            if (facility.contains("Debug")){
                bars.put("debug", count);
            }
        }
        public Map getBars(){
            return this.bars;
        }
    }
}
