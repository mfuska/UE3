package OtwayRees.AuthenticationServer;


import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created on 03.05.15.
 */

public class readFile {
    private BufferedReader in;
    private static myLogger logObj;
    private static Logger logger;

    public readFile(String name) {
        try {
            logObj = myLogger.getInstance();
            logger = logObj.getLogger();
            logger.info("try to start readFileObj");
            in = new BufferedReader(new FileReader(name));
            logger.info("read file:" + name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected HashMap<String, BigInteger> generateDB() throws IOException {
        logger.info("generate HashMAP");
        HashMap<String, BigInteger> hashDb = new HashMap<String, BigInteger>();
        String str;
        try {
            while ((str = in.readLine()) != null) {
                if (! str.isEmpty()) {
                    String[] strArr = str.split(" ");
                    hashDb.put(strArr[0], new BigInteger(strArr[1]));
                }
            }
            return hashDb;
        } finally {
            in.close();
        }
    }

}
