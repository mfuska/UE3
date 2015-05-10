package OtwayRees.Client;


import OtwayRees.ASE_1;

import java.math.BigInteger;
import java.util.logging.Logger;

/**
 * Created by mike on 10.05.15.
 */
public class Client {

    private static final BigInteger aseKey = new BigInteger("12206662641430275342531083440137177420987537474656281130677455820918183752998163166093164005037249045382686745022580789272906252015866907831711854407661378927722554413140315499484167431551861099567618138762767541109068012567386907336622951585890319883024056594038142538679119251941806404348663844009081772384922361677141535054725296238683588641617630238150919937282503291128168821730359033120387627371401211374042263663182072270849754259802807984394540835031499641935768799305837226233500964818886685648970920734799662155817997297683350703959348354983335407407667583452192786333125929238615943010628341854261624692941");
    private static String clientName;

    private static OtwayRees.myLogger logObj;
    private static Logger logger;

    private static ASE_1 aseObj;

    private static void init() {
        logObj = OtwayRees.myLogger.getInstance();
        logger = logObj.getLogger();
        logger.info("CLIENT try to read username");
        readUserName();
        logger.info("CLIENT " + clientName + ": Try to start ASE init");
        aseObj = new ASE_1(aseKey);
        logger.info("CLIENT " + clientName + ": Start ClientCommunication");
        startUserCommunicationSocket();
    }
    private static void readUserName() {
        ResultSetter setter = new ResultSetter() {
            public void setResult(String name, String cmd) {
                clientName = name;
            }
        };
        ClientReadInputThread threadRead = new ClientReadInputThread(logger);
        threadRead.setName("ClientReadInputThread");

        threadRead.setResultSetter(setter);
        threadRead.start();
        /*try {
            threadRead.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
    private static void startUserCommunicationSocket() {
         ClientCommunicationThread ccThread = new ClientCommunicationThread(logger, aseObj);
         ccThread.setName("ClientCommunicationThread ");
         ccThread.start();
    }
    public static void main(String[] args) {
        init();
    }
}




