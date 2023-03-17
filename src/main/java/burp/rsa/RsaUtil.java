package burp.rsa;

import burp.utils.Utils;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;

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
        return Utils.encode(resultBytes, config.OutFormat);
    }

    private IllegalStateException fail(Exception e) {
        e.printStackTrace(Utils.stderr);
        return new IllegalStateException(e);
    }
}

