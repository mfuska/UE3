package OtwayRees.Client;

import OtwayRees.ASE;
import OtwayRees.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created on 14.05.15.
 */

public class ClientConnectionCommunikationThread extends Thread {

    private Socket socket;
    private int port;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Logger logger;
    private ASE aseObj;
    private Message msg_write;
    private ASE ase;
    private int R1;
    private Random rand;

    private static int MAX = 99999999;
    private static int MIN = 10000000;

    public ClientConnectionCommunikationThread(Logger logger, ASE aseObj, int port, Message msg, ASE ase) {
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
            this.socket = new Socket("localhost", this.port);

            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());

            this.R1 = this.rand.nextInt((MAX - MIN) + 1) + MIN;
            String strR1 = Integer.toString(this.R1);
            String msgID = String.valueOf(msg_write.getMsgID());

            // C P1 P2 K1{R1 C P1 P2}
            String str = strR1 + msgID + msg_write.getUserNameA() + msg_write.getUserNameB();
            this.msg_write.setkA(ase.Encrypt(str));
            this.oos.writeObject(msg_write);

            Message authMessage = (Message) ois.readObject();
            if (this.msg_write.getMsgID() != authMessage.getMsgID()) {
                throw new Exception("msgID ERROR: msg_write.getMsgID() != authMessage.getMsgID() ");
            }
            String R1KC =  aseObj.Decrypt(authMessage.getKA());
            String R1_auth = R1KC.substring(0, 8);
            //CHECK R2 == auth.R2
            if (!strR1.equals(R1_auth)) {
                throw new Exception("R1 ERROR: R1 != R1_auth ");
            }
            ASE aseCommunication = new ASE(new BigInteger(R1KC.substring(9,R1KC.length())));
            String msg2Send = aseCommunication.Encrypt("TEST KC");
            oos.writeObject(msg2Send);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.socket.close();
                this.oos.close();
                this.ois.close();
                logger.info(this.getName() + ":Socket is closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
