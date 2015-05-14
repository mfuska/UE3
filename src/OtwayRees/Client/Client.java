package OtwayRees.Client;


import OtwayRees.ASE;
import OtwayRees.Message;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created on 10.05.15.
 */
public class Client {

    private static final Boolean NETZ_WERK_CONFIG = false;

    private static String clientName;
    private static String userNameB;
    private static Boolean startCommunication = false;

    private static HashMap<String,Integer> userPort;
    private static HashMap<String,BigInteger> userKey;
    private static myLogger logObj;
    private static Logger logger;

    private static ASE aseObj;
    private static int msgID; // length == 3


    private static void init() {
        logObj = myLogger.getInstance();
        logger = logObj.getLogger();
        initUserConfig();
        initUserKeyConfig();
        logger.info("CLIENT try to read username");
        readUserInput();
        logger.info("CLIENT " + clientName + ": Try to start ASE init");
        msgID = 100;
    }
    private static void readUserInput() {
        ResultSetter setter = new ResultSetter() {
            public void setResult(String name) {
                clientName = name;
            }
            public void setResult(String nameA, String nameB) {
                clientName = nameA;
                userNameB = nameB;
                System.out.println("nameA:" + clientName + " nameB:" + userNameB);
                startCommunication = true;
            }
        };
        ClientReadInputThread threadRead = new ClientReadInputThread(logger);
        threadRead.setName("ClientReadInputThread");

        threadRead.setResultSetter(setter);
        threadRead.start();
        try {
            threadRead.join();
            aseObj = new ASE(userKey.get(clientName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void startUserListenCommunicationSocket(int port) {
         ClientListenCommunicationThread lcThread = new ClientListenCommunicationThread(logger, aseObj, port);
         lcThread.setName("ClientListenCommunicationThread ");
         //lcThread.setDaemon(true);
         lcThread.start();
    }
    private static void startUserConnectionCommunikationThread(int port) throws Exception {
        if (msgID > 999) {
            throw new Exception("msgID to BIG");
        }
        Message msg = new Message(msgID, clientName, userNameB);
        ClientConnectionCommunikationThread ccThread = new ClientConnectionCommunikationThread(logger, aseObj, port, msg, aseObj);
        ccThread.setName("ClientConnectionCommunikationThread");
        //ccThread.setDaemon(true);
        ccThread.start();
        msgID++;
    }
    private static void initUserConfig() {
        userPort = new HashMap<String, Integer>();
        userPort.put("carina", 50010);
        userPort.put("daniel", 50011);
        userPort.put("michi", 50012);
        userPort.put("port", 50013);
    }
    private static void initUserKeyConfig() {
        userKey = new HashMap<String, BigInteger>();
        userKey.put("carina", new BigInteger("12396894201454229482942565736095426035084782396266143657132324182192315311866278792248919765664030984218214362142106878433372822561550770867643124090221988451372709626356118555503928664610093600452730975143836232120311485526967419922597666880120104428916695124682505236549865933443032448471466693767539417357367951236625041398214661246910366376635207422126564801604594205493742067006458226166714368176529255161117387949463144048830429502583877637402184824674763737656097126873950415777873905921484956837101237385111380834882087452982310410808456828380450244558061379224628009363538308403236086863815326664608020970932"));
        userKey.put("daniel", new BigInteger("15151910848755433202603930469110723260986371382247725485808441072901005107539705044495298456556386312958810912851487617987223405138154954669078774446723754891910210263038299446691262070328133640099673852628243039164957858618075842620457909472111862284600703877280960493164214102691576409261099166094533841095950571420058106224718163904012161706374896192411500740555609496714251561789410515671424391897642989547247549618671228983463553899213549893267954496266879727283524856882787480732129772419486009304718214293108502985719586750191845278825819011390075153908916049010570163818163403825734175093355211067526229252752"));
        userKey.put("michi", new BigInteger("8399903347221661544887309410830227457045669824089123311449336937727977569417009082221423366857594790704454029415509685388787140580405294864116915585151471281156797781385568327750164256849186362148595610595926956134639192445776341242575535164015846962945034070118361622209964013491891363424318199366507771388529973793033175368136576420820566410705849737007626026683197700867322431350517057528559088990547505603557978859347872592918512968724619113794013453235348557620822472695142581580705548133616546204694269105886231315070817116148672116382150180766380216874772597135902807421688831033509509188027891896776283589117"));
    }
    private static void configure() throws Exception {
        if (NETZ_WERK_CONFIG) {
            logger.info("CLIENT NETWORK KONFIG" + clientName + ": Client LISTEN Communication");
            startUserListenCommunicationSocket(userPort.get("port"));
            logger.info("CLIENT NETWORK KONFIG" + clientName + ": Client Connection Communication");
            if (startCommunication) startUserConnectionCommunikationThread(userPort.get("port"));
        } else {
            logger.info("CLIENT NETWORK KONFIG" + clientName + ": Client Connection Communication");
            if (startCommunication) {
                System.out.println("bevor startUserConnectionCommunicationThread on:" + userPort.get(userNameB));
                startUserConnectionCommunikationThread(userPort.get(userNameB));
            } else {
                logger.info("CLIENT LOKAL KONFIG" + clientName + ": Client LISTEN Communication");
                System.out.println("bevor startUserListenCommunication on:" + userPort.get(clientName));
                startUserListenCommunicationSocket(userPort.get(clientName));
            }
        }

    }
    public static void main(String[] args) {
        try {
            init();
            configure();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
