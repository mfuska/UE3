package OtwayRees;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by mike on 03.05.15.
 */
public class generateKeyDB {
    private static OtwayRees.SHA256 sha;
    private static OtwayRees.RSA rsa;
    private static SecureRandom random = new SecureRandom();
    private static int BITLENGTH = 2048;


    public static void main(String[] args) {
        try {
            sha = new OtwayRees.SHA256();
            rsa = new OtwayRees.RSA();
            BigInteger number = new BigInteger(BITLENGTH, random);
            BigInteger number1 = new BigInteger(BITLENGTH, random);
            BigInteger number2 = new BigInteger(BITLENGTH, random);
            System.out.print(sha.hex2String(sha.calculateHash("carina")) + " ");
            System.out.println(rsa.encrypt(number).toString());
            System.out.print(sha.hex2String(sha.calculateHash("daniel")) + " ");
            System.out.println(rsa.encrypt(number1).toString());
            System.out.print(sha.hex2String(sha.calculateHash("michi")) + " ");
            System.out.println(rsa.encrypt(number2).toString());
            System.out.println(number);
            System.out.println(number1);
            System.out.println(number2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
