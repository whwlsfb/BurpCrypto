package burp.aes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import burp.OutFormat;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

// TODO: Implement 256-bit version like: http://securejava.wordpress.com/2012/10/25/aes-256/
public class AesUtil {
    private final Cipher cipher;
    private AesAlgorithms algorithms;

    public AesUtil(AesAlgorithms algorithms) {
        try {
            this.cipher = Cipher.getInstance(algorithms.name().replace('_', '/'));
            this.algorithms = algorithms;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
    }

    public String encrypt(String keyStr, String iv, String plaintext, OutFormat format) {
        try {
            SecretKey key = new SecretKeySpec(keyStr.getBytes(), "AES");
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
            return format == OutFormat.Base64 ? base64(encrypted) : hex(encrypted);
        } catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    public String decrypt(String keyStr, String iv, String cipherText) {
        try {
            SecretKey key = new SecretKeySpec(keyStr.getBytes(), "AES");
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(cipherText));
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            if (algorithms.name().startsWith("AES_ECB_")) {
                cipher.init(encryptMode, key);
            } else {
                cipher.init(encryptMode, key, new IvParameterSpec(iv.getBytes()));
            }
            return cipher.doFinal(bytes);
        } catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            throw fail(e);
        }
    }


    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
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

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}

