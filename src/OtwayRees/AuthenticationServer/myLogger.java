package OtwayRees.AuthenticationServer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * Created on 03.05.15.
 */
public class myLogger {
    private Logger logger = Logger.getLogger("OtwayRees.AuthenticationServer");
    private FileHandler fh;
    private int SIZE = 2048;
    private int ROTATIONCOUNT = 1;

    private static myLogger instance;
    private myLogger() {
        try {
            fh = new FileHandler("./src/OtwayRees/AuthenticationServer/OtwayReesAuthenticationServer.log", this.SIZE, this.ROTATIONCOUNT);
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static myLogger getInstance () {
        if (myLogger.instance == null) {
            myLogger.instance = new myLogger();
        }
        return myLogger.instance;
    }
    public synchronized Logger getLogger() {
        return logger;
    }
}
