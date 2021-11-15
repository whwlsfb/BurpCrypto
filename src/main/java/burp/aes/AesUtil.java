package burp.aes;

import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.io.UnsupportedEncodingException;

public class AesUtil {
    private AesAlgorithms algorithms;
    private AesConfig config;
    private CipherInfo cipherInfo;
    private SymmetricCrypto crypto;

    public void setConfig(AesConfig config) {
        this.config = config;
        try {
            this.cipherInfo = new CipherInfo(algorithms.name().replace("_", "/"));
            this.crypto = new AES(Mode.valueOf(this.cipherInfo.Mode), Padding.valueOf(this.cipherInfo.Padding), this.config.Key, this.config.IV);
        } catch (Exception e) {
            throw fail(e);
        }
    }

    public String encrypt(byte[] plaintext) {
        byte[] encrypted = crypto.encrypt(plaintext);
        return config.OutFormat == OutFormat.Base64 ? Utils.base64(encrypted) : Utils.hex(encrypted);
    }

    public String decrypt(String cipherText) {
        try {
            byte[] decrypted = crypto.decrypt(Utils.base64(cipherText));
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}

