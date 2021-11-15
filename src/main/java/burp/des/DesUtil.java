package burp.des;

import burp.aes.AesAlgorithms;
import burp.aes.AesConfig;
import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.DESede;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

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
    private DesConfig config;
    private CipherInfo cipherInfo;
    private DesJs strEncAlg;
    private SymmetricCrypto crypto;

    public void setConfig(DesConfig config) {
        this.config = config;
        DesAlgorithms algorithms = config.Algorithms;
        try {
            if (algorithms != DesAlgorithms.strEnc) {
                this.cipherInfo = new CipherInfo(algorithms.name().replace("_","/"));
                if (this.cipherInfo.Algorithm.equals("DES")) {
                    this.crypto = new DES(Mode.valueOf(this.cipherInfo.Mode), Padding.valueOf(this.cipherInfo.Padding), this.config.Key, this.config.IV);
                } else {
                    this.crypto = new DESede(Mode.valueOf(this.cipherInfo.Mode), Padding.valueOf(this.cipherInfo.Padding), this.config.Key, this.config.IV);
                }
            } else strEncAlg = new DesJs();
        } catch (Exception e) {
            throw fail(e);
        }
    }

    public String encrypt(byte[] plaintext) {
        if (config.Algorithms != DesAlgorithms.strEnc) {
            byte[] encrypted = this.crypto.encrypt(plaintext);
            return config.OutFormat == OutFormat.Base64 ? Utils.base64(encrypted) : Utils.hex(encrypted);
        } else {
            return strEncAlg.strEnc(new String(plaintext), config.Key1, config.Key2, config.Key3);
        }
    }

    public String decrypt(String cipherText) {
        if (config.Algorithms != DesAlgorithms.strEnc) {
            byte[] decrypted = this.crypto.decrypt(Utils.base64(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } else {
            return strEncAlg.strDec(cipherText, config.Key1, config.Key2, config.Key3);
        }
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}

