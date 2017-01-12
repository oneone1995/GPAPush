package cn.zheteng123.hdu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created on 2016/6/20.
 */
public class MD5 {
    public static String encode(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
