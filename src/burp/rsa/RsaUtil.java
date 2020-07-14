package burp.rsa;

import burp.aes.AesAlgorithms;
import burp.aes.AesConfig;
import burp.utils.OutFormat;

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

import static burp.utils.Utils.base64;
import static burp.utils.Utils.hex;

public class RsaUtil {
    private Cipher cipher;
    private Key key;
    private OutFormat outFormat;

    public void setConfig(RsaConfig config) {
        try {
            this.cipher = Cipher.getInstance("RSA");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(config.Modulus, config.Exponent);
            key = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
            throw fail(e);
        }
        outFormat = config.OutFormat;
    }

    public String encrypt(byte[] plaintext) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext);
        return outFormat == OutFormat.Base64 ? base64(encrypted) : hex(encrypted);
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}

