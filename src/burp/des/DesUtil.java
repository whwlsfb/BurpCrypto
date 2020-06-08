package burp.des;

import burp.aes.AesAlgorithms;
import burp.aes.AesConfig;
import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DesUtil {
    private Cipher cipher;
    private byte[] IV;
    private SecretKey sKey;
    private OutFormat outFormat;
    private CipherInfo cipherInfo;

    public void setConfig(DesConfig config) {
        try {
            DesAlgorithms algorithms = config.Algorithms;
            String algName = algorithms.name().replace('_', '/');
            this.cipher = Cipher.getInstance(algName);
            this.cipherInfo = new CipherInfo(algName);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
        sKey = new SecretKeySpec(config.Key, this.cipherInfo.Algorithm);
        IV = config.IV;
        outFormat = config.OutFormat;
    }

    public String encrypt(byte[] plaintext) {
        byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, sKey, IV, plaintext);
        return outFormat == OutFormat.Base64 ? Utils.base64(encrypted) : Utils.hex(encrypted);
    }

    public String decrypt(String cipherText) {
        byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, sKey, IV, Utils.base64(cipherText));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private byte[] doFinal(int encryptMode, SecretKey key, byte[] iv, byte[] bytes) {
        try {
            if (cipherInfo.Mode.equals("ECB")) {
                cipher.init(encryptMode, key);
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

