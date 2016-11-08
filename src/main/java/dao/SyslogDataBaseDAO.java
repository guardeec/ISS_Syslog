package dao;

import containers.Log;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Guardeec on 31.08.16.
 */
public class SyslogDataBaseDAO extends JdbcDaoSupport implements SyslogDataBaseIMPL, SyslogDataBaseSearchByPageImpl, Test {

    public List getLog(Integer idFrom, Integer idTo, Date dateFrom, Date dateTo, Integer facilityFrom, Integer facilityTo, String facilityName, Integer levelFrom, Integer levelTo, String levelName, String host, String message) {
        List<String> logs;
        try{
            logs = (List<String>) getJdbcTemplate().queryForObject(
                    "SELECT events.id as id, events.date_time, \n" +
                            "facility.descr, severity.descr, \n" +
                            "events.host, events.descr \n" +
                            "FROM events \n" +
                            "JOIN facility ON events.f_id = facility.id \n" +
                            "JOIN severity ON events.s_id = severity.id \n" +
                            ";",
                    new Object[]{},
                    new SearchRowMapper()
            );
        }catch (org.springframework.dao.EmptyResultDataAccessException | CannotGetJdbcConnectionException ignored){
            logs = new LinkedList<>();
            logs.add("Error");
        }
        return logs;
    }

    @Override
    public List getLastLogs(Integer numberOfLogs, Integer idFrom, Integer idTo, Date dateFrom, Date dateTo, Integer facilityFrom, Integer facilityTo, String facilityName, Integer levelFrom, Integer levelTo, String levelName, String host, String message) {
        List<String> logs;
        try{
            logs = (List<String>) getJdbcTemplate().queryForObject(
                    "SELECT events.id as id, events.date_time, \n" +
                            "facility.descr, severity.descr, \n" +
                            "events.host, events.descr \n" +
                            "FROM events \n" +
                            "JOIN facility ON events.f_id = facility.id \n" +
                            "JOIN severity ON events.s_id = severity.id \n" +
                            "ORDER BY id DESC \n" +
                            "LIMIT ? \n" +
                            ";",
                    new Object[]{numberOfLogs},
                    new SearchRowMapper()
            );
        }catch (org.springframework.dao.EmptyResultDataAccessException | CannotGetJdbcConnectionException ignored){
            logs = new LinkedList<>();
            logs.add("Error");
        }
        return logs;
    }

    @Override
    public List getLogOnPage(Integer idFrom, Integer idTo, Timestamp dateFrom, Timestamp dateTo, Integer facility, Integer severity, String host, String message, Integer page, String sortOrder, boolean sortUp) {

        List logs;
//        Integer[] numberOfPagesAndPadding = numberOfPages(idFrom, idTo, dateFrom, dateTo, facility, severity, host, message);

        if (host==null){
            host="";
        }
        if (message==null){
            message="";
        }



        //if (numberOfPagesAndPadding[0]!=0){
            try{
                Integer offset /*= (numberOfPagesAndPadding[0]-page-1)*15+numberOfPagesAndPadding[1]*/;
                offset=(page-1)*15;
                if (offset<0){
                    offset=0;
                }
                String sortUpAsString;
                if (sortUp){
                    sortUpAsString = "ASC";
                }else {
                    sortUpAsString = "DESC";
                }
                logs = (List<String>) getJdbcTemplate().queryForObject(
                        "SELECT events.id as id, events.date_time,              " +
                                "facility.descr, severity.descr,                " +
                                "events.host, events.descr                      " +
                                "FROM events                                    " +
                                "JOIN facility ON events.f_id = facility.id     " +
                                "JOIN severity ON events.s_id = severity.id     " +
                                "WHERE events.id BETWEEN coalesce(?, events.id) AND coalesce(?, events.id)                    " +
                                "AND events.date_time BETWEEN coalesce(?, events.date_time) AND coalesce(?, events.date_time) " +
                                "AND events.f_id = coalesce(?, events.f_id)                " +
                                "AND facility.descr = coalesce(NULL , facility.descr)                                                " +
                                "AND events.s_id = coalesce(? , events.s_id)                 " +
                                "AND severity.descr = coalesce(NULL , severity.descr)                                                " +
                                "AND events.host LIKE '%"+host+"%'"+
                                "AND events.descr LIKE '%"+message+"%'"+
                                "ORDER BY "+sortOrder+" "+sortUpAsString+"                               " +
                                "OFFSET ?                                       " +
                                "LIMIT 15                                       " +
                                ";",
                        new Object[]{idFrom, idTo, dateFrom, dateTo, facility, severity, offset},
                        new SearchRowMapper()
                );
            }catch (org.springframework.dao.EmptyResultDataAccessException | CannotGetJdbcConnectionException ignored){
                logs = new LinkedList<>();
                Log log = new Log();
                log.setMessage("NO LOGS "+idFrom+" "+idTo+" "+dateFrom+" "+dateTo+" "+facility+" "+severity+" "+(page-1)*15);
                logs.add(log);
            }
//        }else {
//            logs = new LinkedList<>();
//            Log log = new Log();
//            log.setMessage("NO LOGS "+host+" "+message+" "+numberOfPagesAndPadding[0]);
//            logs.add(log);
//        }
        return logs;
    }

    @Override
    public Integer[] numberOfPages(Integer idFrom, Integer idTo, Timestamp dateFrom, Timestamp dateTo, Integer facility, Integer severity, String host, String message) {
        Integer numberOfMessages;

        if (host==null){
            host="";
        }
        if (message==null){
            message="";
        }

        try {
            numberOfMessages=getJdbcTemplate().queryForObject(
                    "SELECT count(id) \n" +
                            "FROM ( \n" +
                            "SELECT \n" +
                            "events.id AS id \n" +
                            "FROM events \n" +
                            "JOIN facility ON events.f_id = facility.id \n" +
                            "JOIN severity ON events.s_id = severity.id \n" +
                            "WHERE events.id BETWEEN coalesce(? , events.id) AND coalesce(? , events.id) \n" +
                            "AND events.date_time BETWEEN coalesce(? , events.date_time) AND coalesce(? , events.date_time) \n" +
                            "AND events.f_id = coalesce(? , events.f_id) \n" +
                            "AND facility.descr = coalesce(NULL , facility.descr) \n" +
                            "AND events.s_id = coalesce(? , events.s_id)  \n" +
                            "AND severity.descr = coalesce(NULL , severity.descr) \n" +
                            "AND events.host LIKE '%"+host+"%'"+
                            "AND events.descr LIKE '%"+message+"%'"+
                            ") AS events_count;",
                    new Object[]{idFrom, idTo, dateFrom, dateTo, facility, severity},
                    Integer.class
            );
        }catch (org.springframework.dao.EmptyResultDataAccessException | CannotGetJdbcConnectionException ignored){
            return new Integer[]{0,0};
        }

        return numberOfMessages%15==0 ?
                new Integer[]{numberOfMessages/15,0} :
                new Integer[]{numberOfMessages/15+1,numberOfMessages%15}
                ;
    }

    @Override
    public void deleteLogs() {
        getJdbcTemplate().update("DELETE FROM events WHERE date_time < current_timestamp;");
    }

    private class SearchRowMapper implements org.springframework.jdbc.core.RowMapper<Object> {
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            List<Log> logs = new LinkedList<>();

            do {
                Log log = new Log();
                log.setId(resultSet.getInt(1));
                log.setDate(resultSet.getDate(2));
                log.setTime(resultSet.getTime(2));
                log.setFacility(resultSet.getString(3));
                log.setSeverity(resultSet.getString(4));
                log.setHost(resultSet.getString(5));
                log.setMessage(resultSet.getString(6));
                logs.add(log);
            }while (resultSet.next());

            return logs;
        }
    }

}
