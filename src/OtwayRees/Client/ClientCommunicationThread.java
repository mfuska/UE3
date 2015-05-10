package OtwayRees.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by mike on 10.05.15.
 */
public class ClientCommunicationThread extends Thread {
    private static final int PORT = 5013;

    private Socket socket;
    private Logger logger;
    private OtwayRees.ASE_1 aseObj;

    public ClientCommunicationThread(Logger logger, OtwayRees.ASE_1 aseObj) {

        this.logger = logger;
        this.aseObj = aseObj;
    }

    public void run() {
        try {
            logger.info("try to open the Socket");
            ServerSocket s_Socket = new ServerSocket(this.PORT);
            while (true) {
                Socket s_incoming = s_Socket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Socket is closed");
        System.out.println("run CLIENT THREAD");
    }
}

