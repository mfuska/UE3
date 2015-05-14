package OtwayRees.Client;

import OtwayRees.ASE_1;
import OtwayRees.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by mike on 14.05.15.
 */

public class ClientConnectionCommunikationThread extends Thread {
    private int port;

    private Socket socket;
    private Logger logger;
    private ASE_1 aseObj;
    private Message msg_write;
    private ASE_1 ase;
    private int R1;
    private Random rand;

    private static int MAX = 99999999;
    private static int MIN = 10000000;

    public ClientConnectionCommunikationThread(Logger logger, ASE_1 aseObj, int port, Message msg, ASE_1 ase) {
        this.port = port;
        this.logger = logger;
        this.aseObj = aseObj;
        this.msg_write = msg;
        this.ase = ase;

        this.rand = new Random();
    }

    public void run() {
        try {
            logger.info("try to open the Socket");
            Socket c_socket = new Socket("localhost", this.port);

            ObjectOutputStream oos = new ObjectOutputStream(c_socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(c_socket.getInputStream());

            this.R1 = this.rand.nextInt((MAX - MIN) + 1) + MIN;
            String strR = String.valueOf(this.R1);
            String msgID = String.valueOf(msg_write.getMsgID());

            // C P1 P2 K1{R1 C P1 P2}
            String str = new String(strR + msgID + msg_write.getUserNameA() + msg_write.getUserNameB());
            System.out.println(this.getName() + " write:" + str);
            this.msg_write.setkA(ase.Encrypt(str));
            System.out.println(this.getName() + " write:" + str + " kA:" + msg_write.getKA());
            oos.writeObject(msg_write);
            System.out.println(this.getName() + " after write:" + str);

            //Message msgObj_read = (Message) ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Socket is closed");
        System.out.println("run CLIENT THREAD");
    }
}
