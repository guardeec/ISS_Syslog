package dao;

import java.sql.Date;
import java.util.List;

/**
 * Created by Guardeec on 31.08.16.
 */
public interface SyslogDataBaseIMPL {
    public List getLog(
            Integer idFrom,
            Integer idTo,

            Date dateFrom,
            Date dateTo,

            Integer facilityFrom,
            Integer facilityTo,
            String facilityName,

            Integer levelFrom,
            Integer levelTo,
            String levelName,

            String host,
            String message
            );

    public List getLastLogs(
            Integer numberOfLogs,

            Integer idFrom,
            Integer idTo,

            Date dateFrom,
            Date dateTo,

            Integer facilityFrom,
            Integer facilityTo,
            String facilityName,

            Integer levelFrom,
            Integer levelTo,
            String levelName,

            String host,
            String message
    );
}
