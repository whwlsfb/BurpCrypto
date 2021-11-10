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
import java.util.Arrays;

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

    public String encrypt(byte[] inputArray) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int MAX_ENCRYPT_BLOCK = 117;
        int offSet = 0;
        int inputLength = inputArray.length;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return outFormat == OutFormat.Base64 ? base64(resultBytes) : hex(resultBytes);
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}

