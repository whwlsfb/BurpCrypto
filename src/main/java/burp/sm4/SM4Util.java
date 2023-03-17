package burp.sm4;

import burp.utils.CipherInfo;
import burp.utils.OutFormat;
import burp.utils.Utils;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.io.UnsupportedEncodingException;

public class SM4Util {
    private SM4Config config;
    private CipherInfo cipherInfo;
    private SymmetricCrypto crypto;

    public void setConfig(SM4Config config) {
        this.config = config;
        try {
            this.cipherInfo = new CipherInfo(config.Algorithms.name().replace("_", "/"));
            this.crypto = new SM4(Mode.valueOf(this.cipherInfo.Mode), Padding.valueOf(this.cipherInfo.Padding), this.config.Key, this.config.IV);
        } catch (Exception e) {
            throw fail(e);
        }
    }

    public String encrypt(byte[] plaintext) {
        byte[] encrypted = crypto.encrypt(plaintext);
        return Utils.encode(encrypted, config.OutFormat);
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
        e.printStackTrace(Utils.stderr);
        return new IllegalStateException(e);
    }
}

