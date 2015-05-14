package OtwayRees.AuthenticationServer;


import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * Created on 03.05.15.
 */

public class readFile {
    private BufferedReader in;

    public readFile(String name) {
        try {
            in = new BufferedReader(new FileReader(name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected HashMap<String, BigInteger> generateDB() throws IOException {
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
