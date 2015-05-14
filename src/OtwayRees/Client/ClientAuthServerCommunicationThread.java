package OtwayRees.Client;

import OtwayRees.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created on 14.05.15.
 */
public class ClientAuthServerCommunicationThread extends Thread {
    private final static int PORT = 50001;

    private ResultAuthServerSetter setter;
    private Message msgObj;
    private Logger logger;
    private Random rand;

    public ClientAuthServerCommunicationThread(Logger logger, Message msgObj) {
        this.msgObj = msgObj;
        this.logger = logger;
    }
    public void setResultSetter(ResultAuthServerSetter setter) {
        this.setter = setter;
    }

    public void run() {
        try {
            this.logger.info("try to open the Socket");
            Socket c_socket = new Socket("localhost", PORT);

            ObjectOutputStream oos = new ObjectOutputStream(c_socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(c_socket.getInputStream());


            // C P1 P2 K1{R1 C P1 P2} K2{R2 C P1 P2}
            System.out.println(this.getName() + " write MessageObj to Auth Server");
            oos.writeObject(msgObj);

            Message msgObj_read = (Message) ois.readObject();
            // CK1{R1 KC} K2{R2 KC}
            this.setter.setResultSetter(msgObj_read);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
