package OtwayRees;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created on 10.05.15.
 */
public class AES {
    private Cipher cipherSend;
    private Cipher cipherReceive;

    public AES(BigInteger Key) {
        byte[] KeyBytes = Key.toByteArray();

        try {
            cipherReceive = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipherSend = Cipher.getInstance("AES/CBC/PKCS5Padding");

            AlgorithmParameterSpec IVsend    = new IvParameterSpec(KeyBytes, 0, 16);  // die ersten 128bit als IV
            AlgorithmParameterSpec IVreceive = new IvParameterSpec(KeyBytes, 0, 16);

            SecretKeySpec KeySend    = new SecretKeySpec(KeyBytes, 32, 16, "AES");    // die zweiten 128bit als Key
            SecretKeySpec KeyReceive = new SecretKeySpec(KeyBytes, 32, 16, "AES");

            cipherSend.init(Cipher.ENCRYPT_MODE, KeySend, IVsend);
            cipherReceive.init(Cipher.DECRYPT_MODE, KeyReceive, IVreceive);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public String Encrypt(String PlainText) {
        String CipherTextBase64 = "";
        try {
            CipherTextBase64 = DatatypeConverter.printBase64Binary(cipherSend.doFinal(PlainText.getBytes()));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return CipherTextBase64;
    }

    public String Decrypt(String CipherTextBase64) {
        String PlainText = "";
        byte[] CipherText = DatatypeConverter.parseBase64Binary(CipherTextBase64);
        try {
            PlainText = new String(cipherReceive.doFinal(CipherText));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return PlainText;
    }
}
