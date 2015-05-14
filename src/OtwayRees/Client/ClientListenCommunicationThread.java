package OtwayRees.Client;

import OtwayRees.ASE;
import OtwayRees.Message;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created on 10.05.15.
 */
public class ClientListenCommunicationThread extends Thread {
    private int port;

    private ServerSocket s_Socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Logger logger;
    private ASE aseObj;
    private static int MAX = 99999999;
    private static int MIN = 10000000;
    private int R2;
    private Random rand;
    private Message authMessage;

    public ClientListenCommunicationThread(Logger logger, ASE aseObj, int port) {
        this.port = port;
        this.logger = logger;
        this.aseObj = aseObj;

        this.rand = new Random();
        this.R2 =  rand.nextInt((MAX - MIN) + 1) + MIN;
    }

    private void startAuthServerCommunication(Message messageObj) {
        ResultAuthServerSetter setter = new ResultAuthServerSetter() {
            public void setResultSetter(Message authMsg) {
                authMessage = authMsg;
            }
        };

        ClientAuthServerCommunicationThread threadAuth = new ClientAuthServerCommunicationThread(logger, messageObj);
        threadAuth.setName("ClientAuthServerCommunicationThread");

        threadAuth.setResultSetter(setter);
        threadAuth.start();
        try {
            threadAuth.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            logger.info("try to open the Socket");
            this.s_Socket = new ServerSocket(this.port);
            Socket socket = this.s_Socket.accept();

            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println(this.getName() + " read:");
            Message msg_Input = (Message) this.ois.readObject();
            System.out.println(this.getName() + "after read:");

            this.R2 =  this.rand.nextInt((MAX - MIN) + 1) + MIN;
            String msgID = String.valueOf(msg_Input.getMsgID());
            String strR2 = Integer.toString(this.R2);

            String str = strR2 + msgID + msg_Input.getUserNameA() + msg_Input.getUserNameB();

            // C P1 P2 K1{R1 C P1 P2} K2{R2 C P1 P2}
            msg_Input.setkB(aseObj.Encrypt(str));
            startAuthServerCommunication(msg_Input);
            //CHECK msgID == auth.msgID
            if (msg_Input.getMsgID() != authMessage.getMsgID()) {
                throw new Exception("msgID ERROR: msg_Input.getMsgID() != authMessage.getMsgID() ");
            }
            String R2KC =  aseObj.Decrypt(authMessage.getKB());
            String R2_auth = R2KC.substring(0, 8);
            BigInteger KC = new BigInteger(R2KC.substring(9,R2KC.length()));
            //CHECK R2 == auth.R2
            if (!strR2.equals(R2_auth)) {
                throw new Exception("R2 ERROR: R2 != R2_auth ");
            }
            Message msg_Auth = new Message(authMessage.getMsgID());
            msg_Auth.setkA(authMessage.getKA());
            oos.writeObject(msg_Auth);

            String msgReceived = (String) ois.readObject();
            ASE aseCommunication = new ASE(new BigInteger(R2KC.substring(9,R2KC.length())));
            System.out.println("Decrypt message:" + aseCommunication.Decrypt(msgReceived));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.ois.close();
                this.oos.close();
                this.s_Socket.close();
                logger.info("Socket is closed");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

