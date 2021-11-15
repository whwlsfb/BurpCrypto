package burp.rsa;

import burp.aes.AesAlgorithms;
import burp.aes.AesConfig;
import burp.utils.OutFormat;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

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

