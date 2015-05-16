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

public class ClientCommunicationClientThread extends Thread {

    private Socket socket;
    private int port;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ASE aseObj;
    private Message msg_write;
    private ASE ase;
    private int R1;
    private Random rand;

    private static int MAX = 99999999;
    private static int MIN = 10000000;

    public ClientCommunicationClientThread(ASE aseObj, int port, Message msg, ASE ase) {
        this.port = port;
        this.aseObj = aseObj;
        this.msg_write = msg;
        this.ase = ase;
        this.rand = new Random();
    }

    public void run() {
        try {
            System.out.println(this.getName() + " try to connect ....");
            this.socket = new Socket("localhost", this.port);
            System.out.println(this.getName() + " connection established ....");
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());

            this.R1 = this.rand.nextInt((MAX - MIN) + 1) + MIN;
            String strR1 = Integer.toString(this.R1);
            String msgID = String.valueOf(msg_write.getMsgID());

            // C P1 P2 K1{R1 C P1 P2}
            String userPaddingA = String.format("%-10s", msg_write.getUserNameA());
            String userPaddingB = String.format("%-10s", msg_write.getUserNameB());
            String str = strR1 + msgID + userPaddingA + userPaddingB;

            this.msg_write.setkA(ase.Encrypt(str));
            this.oos.writeObject(msg_write);

            Message authMessage = (Message) ois.readObject();
            if (this.msg_write.getMsgID() != authMessage.getMsgID()) {
                throw new RuntimeException("msgID ERROR: msg_write.getMsgID() != authMessage.getMsgID() ");
            }
            String R1KC =  aseObj.Decrypt(authMessage.getKA());
            String R1_auth = R1KC.substring(0, 8);
            //CHECK R2 == auth.R2
            if (!strR1.equals(R1_auth)) {
                throw new RuntimeException("R1 ERROR: R1 != R1_auth ");
            }
            ASE aseCommunication = new ASE(new BigInteger(R1KC.substring(9,R1KC.length())));
            String msg2Send = aseCommunication.Encrypt(msg_write.getUserNameA() + " send his first message");
            System.out.println(this.getName() + "Encrypt: " + msg_write.getUserNameA() + " send his first message");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
