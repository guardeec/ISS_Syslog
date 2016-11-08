package dao;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Guardeec on 26.10.16.
 */
public interface SyslogDataBaseSearchByPageImpl {
    public List getLogOnPage(
            Integer idFrom,
            Integer idTo,

            Timestamp dateFrom,
            Timestamp dateTo,

            Integer facility,
            Integer severity,

            String host,
            String message,

            Integer page,

            String sortOrder,
            boolean sortUp
    );

    public Integer[] numberOfPages(
            Integer idFrom,
            Integer idTo,

            Timestamp dateFrom,
            Timestamp dateTo,

            Integer facility,
            Integer severity,

            String host,
            String message
    );
}
