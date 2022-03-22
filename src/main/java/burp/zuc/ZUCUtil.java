package burp.zuc;

import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.crypto.symmetric.ZUC;

import java.io.UnsupportedEncodingException;

public class ZUCUtil {
    private ZUCConfig config;
    private SymmetricCrypto crypto;

    public void setConfig(ZUCConfig config) {
        this.config = config;
        try {
            this.crypto = new ZUC(config.Algorithms, this.config.Key, this.config.IV);
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

