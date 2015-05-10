package OtwayRees;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES {

    private static final byte[] SALT = {
            (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
            (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };

    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 128;

    private Cipher ecipher;
    private Cipher dcipher;

    public AES(String passPhrase) throws Exception {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, secret);

        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = ecipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        dcipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    }

    public String encrypt(String encrypt) throws Exception {
        byte[] bytes = encrypt.getBytes("UTF8");
        byte[] encrypted = encrypt(bytes);
        return new BASE64Encoder().encode(encrypted);
    }

    public byte[] encrypt(byte[] plain) throws Exception {
        return ecipher.doFinal(plain);
    }

    public String decrypt(String encrypt) throws Exception {
        byte[] bytes = new BASE64Decoder().decodeBuffer(encrypt);
        byte[] decrypted = decrypt(bytes);
        return new String(decrypted, "UTF8");
    }

    public byte[] decrypt(byte[] encrypt) throws Exception {
        return dcipher.doFinal(encrypt);
    }

    public static void main(String[] args) throws Exception {

        String message = "MESSAGE";
        String password = "PASSWORD";

        AES encrypter = new AES(password);
        String encrypted = encrypter.encrypt(message);
        String decrypted = encrypter.decrypt(encrypted);

        System.out.println("Encrypt(\"" + message + "\", \"" + password + "\") = \"" + encrypted + "\"");
        System.out.println("Decrypt(\"" + encrypted + "\", \"" + password + "\") = \"" + decrypted + "\"");

        String test = new String("eKqpnDL2mcLkPp179RC/vA");
        System.out.println("Decrypt:" + encrypter.decrypt(test));
    }
}
/*
public void CipherInitialise(BigInteger Key) {
        byte[] KeyBytes = Key.toByteArray();

        try {
            CipherReceive = Cipher.getInstance("AES/CBC/PKCS5Padding");
            CipherSend = Cipher.getInstance("AES/CBC/PKCS5Padding");

            AlgorithmParameterSpec IVsend    = new IvParameterSpec(KeyBytes, 0, 16);  // die ersten 128bit als IV
            AlgorithmParameterSpec IVreceive = new IvParameterSpec(KeyBytes, 0, 16);

            SecretKeySpec KeySend    = new SecretKeySpec(KeyBytes, 32, 16, "AES");    // die zweiten 128bit als Key
            SecretKeySpec KeyReceive = new SecretKeySpec(KeyBytes, 32, 16, "AES");

            CipherSend.init(Cipher.ENCRYPT_MODE, KeySend, IVsend);
            CipherReceive.init(Cipher.DECRYPT_MODE, KeyReceive, IVreceive);
        }  catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
}

public String Encrypt(String PlainText) {
        String CipherTextBase64 = "";
        try {
            CipherTextBase64 = DatatypeConverter.printBase64Binary(CipherSend.doFinal(PlainText.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return CipherTextBase64;
}

public String Decrypt(String CipherTextBase64) {
        String PlainText = "";
        byte[] CipherText = DatatypeConverter.parseBase64Binary(CipherTextBase64);
        try {
            PlainText = new String(CipherReceive.doFinal(CipherText));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return PlainText;
}
*/