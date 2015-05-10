package OtwayRees.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Created by mike on 10.05.15.
 */
public class ClientReadInputThread extends Thread {

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
            logger.info("read name");
            String name = br.readLine();
            logger.info("read command");
            System.out.println("Commands:");
            System.out.println("\tStart Chat = 1");
            System.out.println("\tWait for Chat = 2");
            String cmd = br.readLine();
            setter.setResult(name,cmd);
            logger.info(this.getName() + "run done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
