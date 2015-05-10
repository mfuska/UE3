package OtwayRees.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by mike on 10.05.15.
 */
public class ClientReadInputThread extends Thread {

    private String name;
    private ResultSetter setter;
    private Logger logger;
    public ClientReadInputThread(Logger logger) {
        this.logger = logger;
    }
    public void setResultSetter(ResultSetter setter) {
        this.setter = setter;
    }
    public void run() {
        logger.info(this.getName());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your name:");
        try {
            logger.info(this.getName() + ": bevor readLine()");
            this.name = br.readLine();
            logger.info(this.getName() + ": after readLine()");
            setter.setResult(this.name);
            logger.info(this.getName() + "run done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
