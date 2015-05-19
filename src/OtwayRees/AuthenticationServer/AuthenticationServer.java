package OtwayRees.AuthenticationServer;

import OtwayRees.AES;
import OtwayRees.Message;
import OtwayRees.SHA256;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;

/**
 * Created on 11.05.15.
 */
public class AuthenticationServer {

    private static HashMap<String, BigInteger> key_db;
    private static HashMap<String, Integer> msgID_db;
    private static SHA256 sha;
    private static RSA rsa;

    private static BigInteger n = new BigInteger("22848123644830532351795416657590913738433341265857728831931335968063210081939119101238057675778130286924789483423876829824708448280871724429576180003439804823186333869684468602956713936139324776322791232842648043453517397299359891466840699565891322515648297390635628586182277133028919344825602151293987159922425434997050936119472248274544269006100694419152658205767425516137636956984803496400362398896611335580717989006567455191631959768762009628444114896178281732687578790182228738608916753026098603511044186270837815611241208179491941079809556067822130996638218123549486241874869044136877598218914976816245685560613");
    private static BigInteger d = new BigInteger("21172262217534495471634887981071702875246911135321114362317317444199135576485995743140321385629581035521041020009033673730175993165652071724493971521566433417948732104092921224004169207344571671972826213750004053876926187313885686082384541322254299348083084372694839923079324508122835525142420596732896535118642519815835001993210034528244334536074373100429795492028598092715711589418466101867296242292170945305793366528590678636818718652408259997920316753385801513058072696999364111145817720650173858777644355231643555690036235437807806822774270225704189404159224338741079240815655459826420463019295163812707560281273");
    private static BigInteger e = new BigInteger("65537");


    private static final int PORT = 50001;
    private static ServerSocket s_Socket;
    /*
    * ---------------------------
    * Konfigurationsparameter muss angepasst werden
    * keyFile  path + dateiname
    * ---------------------------
     */
    private static final String keyFile = new String("/Users/mike/2sem/Kryptographische Protokolle/UE3/src/OtwayRees/AuthenticationServer/key.db");

    private static void init() {
        try {
            readFile file = new readFile(keyFile);
            key_db = file.generateDB();
            msgID_db = new HashMap<String, Integer>();
            sha = new OtwayRees.SHA256();
            rsa = new RSA(n,e);
            rsa.setPrivateKey(d);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
    public static void main(String[] args) {
        init();
        try {
            System.out.println("AUTH-SERVER: up and running");
            s_Socket = new ServerSocket(PORT);

            while (true) {
                Socket s_incoming = s_Socket.accept();

                Runnable r = new AuthServerThread(s_incoming, sha, rsa,key_db, msgID_db);
                Thread t = new Thread(r);
                t.setName("AuthServerThread");
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("AUTH-SERVER: closed");
                s_Socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}

class AuthServerThread implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private static int BITLENGTH = 2048;
    private HashMap<String, BigInteger> keydb;
    private HashMap<String, Integer> msgIDdb;
    private OtwayRees.SHA256 sha;
    private RSA rsa;
    private Random rand;


    public AuthServerThread(Socket s, OtwayRees.SHA256 sha, RSA rsa, HashMap<String, BigInteger> keydb, HashMap<String,Integer> msgIDdb) {
        this.socket = s;
        this.rsa = rsa;
        this.sha = sha;
        this.keydb = keydb;
        this.msgIDdb = msgIDdb;
        this.rand = new Random();
    }

    protected BigInteger retunPrivateKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return this.keydb.get(this.sha.hex2String(this.sha.calculateHash(key)));
    }
    protected void check_ReplacementAttack(Message msgObj) throws Exception {
        if (msgIDdb.isEmpty()) {
            msgIDdb.put(msgObj.getUserNameA(), msgObj.getMsgID());
        } else if (! msgIDdb.containsKey(msgObj.getUserNameA())) {
            msgIDdb.put(msgObj.getUserNameA(), msgObj.getMsgID());
        } else {
            if (msgObj.getMsgID() <= msgIDdb.get(msgObj.getMsgID() )) {
                throw new RuntimeException("Replacement attack ;-)");
            }
            msgIDdb.put(msgObj.getUserNameA(), msgObj.getMsgID());
        }
    }

    public void run() {
        try {
            System.out.println(this.getClass().getName() + ": getMMessage");
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            Message msgObj = (Message)ois.readObject();
            System.out.println(this.getClass().getName() + "Count receive Bytes: " + msgObj.sizeof());

            check_ReplacementAttack(msgObj);

            AES AESUserA = new AES(new BigInteger(rsa.decrypt(retunPrivateKey(msgObj.getUserNameA())))) ;
            AES AESUserB = new AES(new BigInteger(rsa.decrypt(retunPrivateKey(msgObj.getUserNameB())))) ;
            Message msgSend = new Message(msgObj.getMsgID());

            System.out.println(this.getClass().getName() + ": UserA:" + msgObj.getUserNameA() + " UserB:" + msgObj.getUserNameB());

            String KA = AESUserA.Decrypt(msgObj.getKA());
            String R1 = KA.substring(0, 8);
            String msgID_A = KA.substring(8, 11);
            String userA_A = KA.substring(11, 21);
            String userB_A = KA.substring(21, 31);

            String KB = AESUserB.Decrypt(msgObj.getKB());
            String R2 = KB.substring(0, 8);
            String msgID_B = KB.substring(8, 11);
            String userA_B = KB.substring(11, 21);
            String userB_B = KB.substring(21, 31);

            if (msgObj.getMsgID() != Integer.valueOf(msgID_A) || !msgID_A.equals(msgID_B)) {
                throw new RuntimeException("msgID do not agree");
            }
            if (! msgObj.getUserNameA().equals(userA_A.trim()) || ! (userA_A.trim()).equals(userA_B.trim())) {
                throw new RuntimeException("user A do not agree");
            }

            if (! msgObj.getUserNameB().equals(userB_A.trim()) || ! (userB_A.trim()).equals(userB_B.trim())) {
                throw new RuntimeException("user B do not agree");
            }

            String KC = (new BigInteger(BITLENGTH, this.rand)).toString();
            String R1KC = R1 + KC;
            String R2KC = R2 + KC;
            msgSend.setkA(AESUserA.Encrypt(R1KC));
            msgSend.setkB(AESUserB.Encrypt(R2KC));

            System.out.println(this.getClass().getName() + ": sendMMessage back");
            System.out.println(this.getClass().getName() + "Count send Bytes: " + msgSend.sizeof());
            oos.writeObject(msgSend);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println(this.getClass().getName() + ": close()");
                this.ois.close();
                this.oos.flush();
                this.oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
