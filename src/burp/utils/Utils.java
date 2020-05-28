package burp.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

public class Utils {
    private static MessageDigest md;

    public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
        byte[] bt3 = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] MD5(byte[] src) {
        if (md == null) {
            try {
                md = MessageDigest.getInstance("md5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5 not found!");
            }
        }
        byte[] secretBytes = null;
        secretBytes = md.digest(src);
        return secretBytes;
    }

    public static byte[] StringKeyToByteKey(String value, KeyFormat format) {
        try {
            switch (format) {
                case HEX:
                    return hex(value);
                case Base64:
                    return base64(value);
                case UTF8String:
                    return value.getBytes("UTF-8");
            }
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    public static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    public static BigInteger[] getBase64PublicKeyME(String base64Str) throws Exception {
        BigInteger[] result = new BigInteger[2];
        byte[] decoded = Base64.decodeBase64(base64Str);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        result[0] = pubKey.getModulus();
        result[1] = pubKey.getPublicExponent();
        return result;
    }


    public static String[] GetOutFormats() {
        ArrayList<String> strs = new ArrayList<String>();
        OutFormat[] items = OutFormat.values();
        for (OutFormat item : items) {
            strs.add(item.name());
        }
        return strs.toArray(new String[strs.size()]);
    }

    public static String[] GetPublicKeyFormats() {
        ArrayList<String> strs = new ArrayList<String>();
        PublicKeyFormat[] items = PublicKeyFormat.values();
        for (PublicKeyFormat item : items) {
            strs.add(item.name());
        }
        return strs.toArray(new String[strs.size()]);
    }
}
