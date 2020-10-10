package burp.des;

import burp.aes.AesAlgorithms;
import burp.aes.AesConfig;
import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DesUtil {
    private Cipher cipher;
    private byte[] IV;
    private SecretKey sKey;
    private DesConfig config;
    private OutFormat outFormat;
    private CipherInfo cipherInfo;
    private DesJs strEncAlg;

    public void setConfig(DesConfig config) {
        this.config = config;
        DesAlgorithms algorithms = config.Algorithms;
        String algName = algorithms.name().replace('_', '/');
        try {
            if (algorithms != DesAlgorithms.strEnc) {
                this.cipher = Cipher.getInstance(algName);
                this.cipherInfo = new CipherInfo(algName);
            } else strEncAlg = new DesJs();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
        if (algorithms != DesAlgorithms.strEnc) {
            try {
                DESKeySpec desKey = new DESKeySpec(config.Key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                sKey = keyFactory.generateSecret(desKey);
            } catch (Exception ex) {
                throw fail(ex);
            }
            IV = config.IV;
            outFormat = config.OutFormat;
        }
    }

    public String encrypt(byte[] plaintext) {
        if (config.Algorithms != DesAlgorithms.strEnc) {
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, sKey, IV, plaintext);
            return outFormat == OutFormat.Base64 ? Utils.base64(encrypted) : Utils.hex(encrypted);
        } else {
            return strEncAlg.strEnc(new String(plaintext), config.Key1, config.Key2, config.Key3);
        }
    }

    public String decrypt(String cipherText) {
        if (config.Algorithms != DesAlgorithms.strEnc) {
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, sKey, IV, Utils.base64(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } else {
            return strEncAlg.strDec(cipherText, config.Key1, config.Key2, config.Key3);
        }
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

