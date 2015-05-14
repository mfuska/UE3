package OtwayRees.Client;

import OtwayRees.ASE_1;
import OtwayRees.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by mike on 10.05.15.
 */
public class ClientListenCommunicationThread extends Thread {
    private int port;

    private Socket socket;
    private Logger logger;
    private OtwayRees.ASE_1 aseObj;
    private static int MAX = 99999999;
    private static int MIN = 10000000;
    private int R2;
    private Random rand;

    public ClientListenCommunicationThread(Logger logger, ASE_1 aseObj, int port) {
        this.port = port;
        this.logger = logger;
        this.aseObj = aseObj;

        this.rand = new Random();
        this.R2 =  rand.nextInt((MAX - MIN) + 1) + MIN;
    }

    public void run() {
        try {
            logger.info("try to open the Socket");
            ServerSocket s_Socket = new ServerSocket(this.port);
            Socket socket = s_Socket.accept();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println(this.getName() + " read:");
            Message msg_Input = (Message) ois.readObject();
            System.out.println(this.getName() + "after read:");

            String strR2 = String.valueOf(this.R2);
            String msgID = String.valueOf(msg_Input.getMsgID());

            this.R2 =  this.rand.nextInt((MAX - MIN) + 1) + MIN;
            String str = new String(strR2 + msgID + msg_Input.getUserNameA() + msg_Input.getUserNameB());
            msg_Input.setkB(aseObj.Encrypt(str));
            System.out.println(this.getName() + "bevor write to Auth Server:" + str + " kA:" + msg_Input.getKA() + " kB:" + msg_Input.getKB());

            //WRITE TO AUTH SERVER
            //oos.writeObject(msg_Input);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        logger.info("Socket is closed");
        System.out.println("run CLIENT THREAD");
    }
}

