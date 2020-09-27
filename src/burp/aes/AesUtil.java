package burp.aes;

import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AesUtil {
    private Cipher cipher;
    private AesAlgorithms algorithms;
    private byte[] IV;
    private SecretKey sKey;
    private OutFormat outFormat;
    private CipherInfo cipherInfo;
    private boolean zeroPaddingMode = false;

    public void setConfig(AesConfig config) {
        try {
            this.algorithms = config.Algorithms;
            String algorithmsName = algorithms.name();
            if (algorithmsName.contains("ZeroPadding")) {
                zeroPaddingMode = true;
                algorithmsName = algorithmsName.replace("ZeroPadding", "NoPadding");
            }
            String algName = algorithmsName.replace('_', '/');
            this.cipherInfo = new CipherInfo(algName);
            this.cipher = Cipher.getInstance(algName);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
        sKey = new SecretKeySpec(config.Key, "AES");
        IV = config.IV;
        outFormat = config.OutFormat;
    }

    public String encrypt(byte[] plaintext) {
        byte[] dataBytes;
        if (zeroPaddingMode) {
            int blockSize = cipher.getBlockSize();
            int length = plaintext.length;
            if (length % blockSize != 0) {
                length = length + (blockSize - (length % blockSize));
            }
            dataBytes = new byte[length];
            System.arraycopy(plaintext, 0, dataBytes, 0, plaintext.length);
        } else dataBytes = plaintext;
        byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, sKey, IV, dataBytes);
        return outFormat == OutFormat.Base64 ? Utils.base64(encrypted) : Utils.hex(encrypted);
    }

    public String decrypt(String cipherText) {
        try {
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, sKey, IV, Utils.base64(cipherText));
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, byte[] iv, byte[] bytes) {
        try {
            if (cipherInfo.Mode.equals("ECB")) {
                cipher.init(encryptMode, key);
            } else if (cipherInfo.Mode.equals("GCM")) {
                cipher.init(encryptMode, key, new GCMParameterSpec(128, iv));
            } else {
                cipher.init(encryptMode, key, new IvParameterSpec(iv));
            }
            return cipher.doFinal(bytes);
        } catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            throw fail(e);
        }
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}

