package OtwayRees;


/**
 * Created by mike on 15.05.15.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class LoggerClass {

    // our log4j category reference
    private static final Logger log = LogManager.getLogger(LoggerClass.class.getName());

    public static void main(String[] args)
    {
        // call our constructor
        new LoggerClass();

        // Log4J is now loaded; try it
        log.info("leaving the main method of Log4JDemo");
    }

    public LoggerClass()
    {
        initializeLogger();
        log.info( "Log4JExample - leaving the constructor ..." );
        log.debug( "Log4JExample - debug leaving the constructor ..." );
    }

    private void initializeLogger()
    {
        //    log.info("Logging initialized.");
    }
}