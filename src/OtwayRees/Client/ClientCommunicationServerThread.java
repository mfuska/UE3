package OtwayRees.Client;

import OtwayRees.AES;
import OtwayRees.Message;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created on 10.05.15.
 */
public class ClientCommunicationServerThread extends Thread {
    private int port;

    private ServerSocket s_Socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private AES AESObj;
    private static int MAX = 99999999;
    private static int MIN = 10000000;
    private int R2;
    private Random rand;
    private Message authMessage;

    public ClientCommunicationServerThread(AES AESObj, int port) {
        this.port = port;
        this.AESObj = AESObj;

        this.rand = new Random();
        this.R2 =  rand.nextInt((MAX - MIN) + 1) + MIN;
    }

    private void startAuthServerCommunication(Message messageObj) {
        ResultAuthServerSetter setter = new ResultAuthServerSetter() {
            public void setResultSetter(Message authMsg) {
                authMessage = authMsg;
            }
        };

        ClientCommunicationAuthServerThread threadAuth = new ClientCommunicationAuthServerThread(messageObj);
        threadAuth.setName("ClientCommunicationAuthServerThread");

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
            System.out.println(this.getName() + " is listening");
            this.s_Socket = new ServerSocket(this.port);
            Socket socket = this.s_Socket.accept();
            System.out.println(this.getName() + " new connection is established");

            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());

            Message msg_Input = (Message) this.ois.readObject();

            this.R2 =  this.rand.nextInt((MAX - MIN) + 1) + MIN;
            String msgID = String.valueOf(msg_Input.getMsgID());
            String strR2 = Integer.toString(this.R2);

            String userPaddingA = String.format("%-10s", msg_Input.getUserNameA());
            String userPaddingB = String.format("%-10s", msg_Input.getUserNameB());
            String str = strR2 + msgID + userPaddingA + userPaddingB;

            msg_Input.setkB(AESObj.Encrypt(str));
            // C P1 P2 K1{R1 C P1 P2} K2{R2 C P1 P2}
            startAuthServerCommunication(msg_Input);
            //CHECK msgID == auth.msgID
            if (msg_Input.getMsgID() != authMessage.getMsgID()) {
                throw new RuntimeException("msgID ERROR: msg_Input.getMsgID() != authMessage.getMsgID() ");
            }
            String R2KC =  AESObj.Decrypt(authMessage.getKB());
            String R2_auth = R2KC.substring(0, 8);

            //CHECK R2 == auth.R2
            if (!strR2.equals(R2_auth)) {
                throw new RuntimeException("R2 ERROR: R2 != R2_auth ");
            }
            Message msg_Auth = new Message(authMessage.getMsgID());
            msg_Auth.setkA(authMessage.getKA());
            oos.writeObject(msg_Auth);

            String msgReceived = (String) ois.readObject();
            AES AESCommunication = new AES(new BigInteger(R2KC.substring(9,R2KC.length())));
            System.out.println(this.getName() + "Decrypt: " + AESCommunication.Decrypt(msgReceived));
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
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

