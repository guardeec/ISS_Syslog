package containers;

import dao.SyslogDataBaseDAO;
import dao.SyslogDataBaseIMPL;
import dao.SyslogDataBaseSearchByPageImpl;
import dao.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Guardeec on 31.08.16.
 */
public class Spring_DAO {
    private static Spring_DAO ourInstance = new Spring_DAO();

    public static Spring_DAO getInstance() {
        return ourInstance;
    }

    private ApplicationContext appContext;
    private SyslogDataBaseIMPL syslogDataBaseDAO;
    private SyslogDataBaseSearchByPageImpl syslogDataBaseSearchByPage;
    private Test deleteAllLogs;

    private Spring_DAO() {
        System.out.println("Инициализация SPRING контейнеров");
        appContext = new ClassPathXmlApplicationContext("JDBC_config.xml");
        syslogDataBaseDAO = (SyslogDataBaseDAO) appContext.getBean("SyslogDAO");
        syslogDataBaseSearchByPage = (SyslogDataBaseDAO) appContext.getBean("SyslogDAO");
        deleteAllLogs = (SyslogDataBaseDAO) appContext.getBean("SyslogDAO");
        System.out.println("Инициализация SPRING контейнеров - DONE");
    }

    public SyslogDataBaseIMPL getSyslogDataBaseDAO(){
        return syslogDataBaseDAO;
    }

    public SyslogDataBaseSearchByPageImpl getSyslogDataBaseSearchByPage() {
        return syslogDataBaseSearchByPage;
    }

    public Test getDeleteAllLogs() {
        return deleteAllLogs;
    }
}
