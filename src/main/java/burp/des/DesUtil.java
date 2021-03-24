package burp.des;

import burp.aes.AesAlgorithms;
import burp.aes.AesConfig;
import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class DesUtil {
    private Cipher cipher;
    private byte[] IV;
    private SecretKey sKey;
    private DesConfig config;
    private OutFormat outFormat;
    private CipherInfo cipherInfo;
    private DesJs strEncAlg;
    private boolean zeroPaddingMode;

    public void setConfig(DesConfig config) {
        this.config = config;
        DesAlgorithms algorithms = config.Algorithms;
        try {
            if (algorithms != DesAlgorithms.strEnc) {
                String algorithmsName = algorithms.name();
                if (algorithmsName.contains("ZeroPadding")) {
                    zeroPaddingMode = true;
                    algorithmsName = algorithmsName.replace("ZeroPadding", "NoPadding");
                }
                String algName = algorithmsName.replace('_', '/');
                this.cipher = Cipher.getInstance(algName);
                this.cipherInfo = new CipherInfo(algName);
            } else strEncAlg = new DesJs();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
        if (algorithms != DesAlgorithms.strEnc) {
            try {
                sKey = new SecretKeySpec(this.config.Key, this.cipherInfo.Algorithm);
            } catch (Exception ex) {
                throw fail(ex);
            }
            IV = config.IV;
            outFormat = config.OutFormat;
        }
    }

    public String encrypt(byte[] plaintext) {
        if (config.Algorithms != DesAlgorithms.strEnc) {
            byte[] dataBytes;
            if (zeroPaddingMode) {
                dataBytes = Utils.ZeroPadding(plaintext, cipher.getBlockSize());
            } else dataBytes = plaintext;
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, sKey, IV, dataBytes);
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

