package OtwayRees.Client;

import OtwayRees.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created on 14.05.15.
 */
public class ClientCommunicationAuthServerThread extends Thread {
    private final static int PORT = 50001;

    private ResultAuthServerSetter setter;
    private Message msgObj;
    private Random rand;

    public ClientCommunicationAuthServerThread(Message msgObj) {
        this.msgObj = msgObj;
    }
    public void setResultSetter(ResultAuthServerSetter setter) {
        this.setter = setter;
    }

    public void run() {
        try {
            Socket c_socket = new Socket("localhost", PORT);

            ObjectOutputStream oos = new ObjectOutputStream(c_socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(c_socket.getInputStream());


            // C P1 P2 K1{R1 C P1 P2} K2{R2 C P1 P2}
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
