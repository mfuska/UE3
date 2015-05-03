package OtwayRees;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by mike on 01.05.15.
 */

public class AuthenticationServer {
    private static HashMap<String, BigInteger> db;
    private static OtwayRees.SHA256 sha;
    private static OtwayRees.RSA rsa;

    private static BigInteger n = new BigInteger("22848123644830532351795416657590913738433341265857728831931335968063210081939119101238057675778130286924789483423876829824708448280871724429576180003439804823186333869684468602956713936139324776322791232842648043453517397299359891466840699565891322515648297390635628586182277133028919344825602151293987159922425434997050936119472248274544269006100694419152658205767425516137636956984803496400362398896611335580717989006567455191631959768762009628444114896178281732687578790182228738608916753026098603511044186270837815611241208179491941079809556067822130996638218123549486241874869044136877598218914976816245685560613");
    private static BigInteger d = new BigInteger("21172262217534495471634887981071702875246911135321114362317317444199135576485995743140321385629581035521041020009033673730175993165652071724493971521566433417948732104092921224004169207344571671972826213750004053876926187313885686082384541322254299348083084372694839923079324508122835525142420596732896535118642519815835001993210034528244334536074373100429795492028598092715711589418466101867296242292170945305793366528590678636818718652408259997920316753385801513058072696999364111145817720650173858777644355231643555690036235437807806822774270225704189404159224338741079240815655459826420463019295163812707560281273");
    private static BigInteger e = new BigInteger("65537");

    private static OtwayRees.myLogger logObj;
    private static Logger logger;

    private static final int PORT = 5013;

    public AuthenticationServer() {
        try {
            logObj = OtwayRees.myLogger.getInstance();
            logger = logObj.getLogger();
            logger.info("try to start AuthenticationServer");
            logger.info("read db file: /Users/mike/2sem/Kryptographische Protokolle/UE3/src/OtwayRees/AuthenticationServer/key.db");
            OtwayRees.readFile file = new OtwayRees.readFile("/Users/mike/2sem/Kryptographische Protokolle/UE3/src/OtwayRees/AuthenticationServer/key.db");
            db = file.generateDB();
            logger.info("init DB finished");
            sha = new OtwayRees.SHA256();
            rsa = new OtwayRees.RSA(n,e);
            rsa.setPrivateKey(d);
            logger.info("started AiuthenticationServer");
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        AuthenticationServer authServ = new AuthenticationServer();
        try {
            ServerSocket s_Socket = new ServerSocket(AuthenticationServer.PORT);

            int i = 1;
            while (true) {
                Socket s_incoming = s_Socket.accept();
                Runnable r = new AuthServerThread(s_incoming, i, logger, sha, rsa,db);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class AuthServerThread implements Runnable {
    private Socket socket;
    private int anzahlClient;

    private Logger logger;
    private HashMap<String, BigInteger> db;
    private OtwayRees.SHA256 sha;
    private OtwayRees.myLogger logObj;
    private OtwayRees.RSA rsa;

    public AuthServerThread(Socket s, int i, Logger logger, OtwayRees.SHA256 sha, OtwayRees.RSA rsa, HashMap<String, BigInteger> db) {
        this.socket = s;
        this.anzahlClient = i;
        this.rsa = rsa;
        this.logger = logger;
        this.sha = sha;
        this.db = db;
    }

    protected BigInteger retunPrivateKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return this.db.get(this.sha.hex2String(this.sha.calculateHash(key)));
    }
    public void run() {
        try {
            System.out.println(rsa.decrypt(retunPrivateKey("daniel")));
            System.out.println(rsa.decrypt(retunPrivateKey("carina")));
            System.out.println(rsa.decrypt(retunPrivateKey("michi")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
