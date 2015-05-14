package OtwayRees.Client;


import OtwayRees.ASE_1;
import OtwayRees.Message;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by mike on 10.05.15.
 */
public class Client {

    private static final Boolean NETZ_WERK_CONFIG = false;

    private static String clientName;
    private static String userNameB;
    private static Boolean startCommunication = false;

    private static HashMap<String,Integer> userPort;
    private static HashMap<String,BigInteger> userKey;
    private static OtwayRees.myLogger logObj;
    private static Logger logger;

    private static ASE_1 aseObj;
    private static int msgID; // length == 3


    private static void init() {
        logObj = OtwayRees.myLogger.getInstance();
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
            aseObj = new ASE_1(userKey.get(clientName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void startUserListenCommunicationSocket(int port) {
        System.out.println("startUserListenCommunicationSocket on:" + port);
         ClientListenCommunicationThread lcThread = new ClientListenCommunicationThread(logger, aseObj, port);
         lcThread.setName("ClientListenCommunicationThread ");
         //lcThread.setDaemon(true);
         lcThread.start();
        System.out.println("end startUserListenCommunicationSocket on:" + port);
    }
    private static void startUserConnectionCommunikationThread(int port) throws Exception {
        System.out.println("startUserConnectionCommunikationThread:" + port);
        if (msgID > 999) {
            throw new Exception("msgID to BIG");
        }
        Message msg = new Message(msgID, clientName, userNameB);
        ClientConnectionCommunikationThread ccThread = new ClientConnectionCommunikationThread(logger, aseObj, port, msg, aseObj);
        ccThread.setName("ClientConnectionCommunikationThread");
        //ccThread.setDaemon(true);
        ccThread.start();
        msgID++;
        System.out.println("end startUserConnectionCommunikationThread:" + port);
    }
    private static void initUserConfig() {
        System.out.println("initUserConfig");
        userPort = new HashMap<String, Integer>();
        userPort.put("carina", 50010);
        userPort.put("daniel", 50011);
        userPort.put("michi", 50012);
        userPort.put("port", 50013);
        System.out.println("end initUserConfig");
    }
    private static void initUserKeyConfig() {
        System.out.println("initUserKeyConfig");
        userKey = new HashMap<String, BigInteger>();
        userKey.put("carina", new BigInteger("4387591534184637826682882383198527817335641845998774346383379124659733172590978822091815316223258804836022498194929843026510957414126905266818849706762043495946653374913160350614014280739501739253179300035242707304831609293971861155441192459303078199677366291696378730061569750943477878771169525721293006089169609592975871042541836324923486991837279739446984031066708560599400742794663345982016387918255152378454927273087055801233032339324687711638551047972945647554933215755423428808628130783284912562816172044119202356806588383536998983861147126911976120892180060663243397395133685729955552900995149930405553816551"));
        userKey.put("daniel", new BigInteger("20209965623765091047138646396912761735870453588838063624228169829134036668226779231748043108567852494007670284032718591428519546765405393886195039362904661817478647326524145586748485771308230660511709113992294134762012433938078695948116176775109516321905327664006999064666457781632609641979732334537365962779776174354573611320796784629946284482777204870924931536249945166493332114999417933360998498031379301506891163020766691250396748036303267853195203378363087928001535631348994862944865004887838307624356725063869126899220741141386217801465874302703138757670199548030745233699487674340179413167918896470015196656930"));
        userKey.put("michi", new BigInteger("12206662641430275342531083440137177420987537474656281130677455820918183752998163166093164005037249045382686745022580789272906252015866907831711854407661378927722554413140315499484167431551861099567618138762767541109068012567386907336622951585890319883024056594038142538679119251941806404348663844009081772384922361677141535054725296238683588641617630238150919937282503291128168821730359033120387627371401211374042263663182072270849754259802807984394540835031499641935768799305837226233500964818886685648970920734799662155817997297683350703959348354983335407407667583452192786333125929238615943010628341854261624692941"));
        System.out.println("end initUserKeyConfig");
    }
    private static void configure() throws Exception {
        if (NETZ_WERK_CONFIG) {
            logger.info("CLIENT NETWORK KONFIG" + clientName + ": Client LISTEN Communication");
            startUserListenCommunicationSocket(userPort.get("port"));
            logger.info("CLIENT NETWORK KONFIG" + clientName + ": Client Connection Communication");
            if (startCommunication) startUserConnectionCommunikationThread(userPort.get("port"));
        } else {
            logger.info("CLIENT LOKAL KONFIG" + clientName + ": Client LISTEN Communication");
            System.out.println("bevor startUserListenCommunication on:" + userPort.get(clientName));
            startUserListenCommunicationSocket(userPort.get(clientName));
            logger.info("CLIENT NETWORK KONFIG" + clientName + ": Client Connection Communication");
            if (startCommunication) {
                System.out.println("bevor startUserConnectionCommunicationThread on:" + userPort.get(userNameB));
                startUserConnectionCommunikationThread(userPort.get(userNameB));
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
