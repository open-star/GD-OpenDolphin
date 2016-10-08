/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tomohiro
 */
public class EncryptUtil {

    /**
     *
     * @param text
     * @return
     */
    public static byte[] encryptWithBlowfish(String text) {
        byte[] encrypted = null;
        try {
            SecretKeySpec sksSpec = new SecretKeySpec("forestRiver".getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, sksSpec);
            encrypted = cipher.doFinal(text.getBytes());
        } catch (Exception ex) {
            System.err.println("Password can't encrypt: " + ex.toString());
        }
        return encrypted;
    }

    /**
     *
     * @param text
     * @return
     */
    public static String decodeWithBlowfish(byte[] text) {
        SecretKeySpec sksSpec = new SecretKeySpec("forestRiver".getBytes(), "Blowfish");
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, sksSpec);

            return new String(cipher.doFinal(text));
        } catch (Exception ex) {
            System.err.println("Password can't decode: " + ex.toString());
        }
        return null;
    }

    /**
     *
     * @param password
     * @return
     */
    public static String createPasswordHash(String password) {
        byte[] passwordBytes = null;

        if (password == null) {
            return null;
        }

        try {
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            passwordBytes = password.getBytes();
        }
        String passwordHash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(passwordBytes);
            byte[] hashedBytes = messageDigest.digest();
            passwordHash = encodeBase16(hashedBytes);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("Password hash can't caliculation: " + ex.toString());
        }

        passwordBytes = null;

        return passwordHash;
    }

    /**
     * MEMO: from org.jboss.security.util
     * @param hashedBytes
     * @return
     */
    private static String encodeBase16(byte[] hashedBytes) {

        StringBuilder sb = new StringBuilder(hashedBytes.length * 2);
        for (int i = 0; i < hashedBytes.length; i++) {
            byte b = hashedBytes[i];
            // top 4 bits
            char c = (char) ((b >> 4) & 0xf);
            if (c > 9) {
                c = (char) ((c - 10) + 'a');
            } else {
                c = (char) (c + '0');
            }
            sb.append(c);
            // bottom 4 bits
            c = (char) (b & 0xf);
            if (c > 9) {
                c = (char) ((c - 10) + 'a');
            } else {
                c = (char) (c + '0');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
