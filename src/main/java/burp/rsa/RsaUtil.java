package burp.rsa;

import burp.utils.OutFormat;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;

import static burp.utils.Utils.base64;
import static burp.utils.Utils.hex;

public class RsaUtil {
    private RsaConfig config;
    private AsymmetricCrypto crypto;

    public void setConfig(RsaConfig config) {
        this.config = config;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(config.Modulus, config.Exponent);
            crypto = new RSA(null, keyFactory.generatePublic(keySpec));
        } catch (Exception e) {
            throw fail(e);
        }
    }

    public String encrypt(byte[] inputArray) throws Exception {
        byte[] resultBytes = this.crypto.encrypt(inputArray, KeyType.PublicKey);
        return config.OutFormat == OutFormat.Base64 ? base64(resultBytes) : hex(resultBytes);
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}

