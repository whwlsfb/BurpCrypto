package burp.pbkdf2;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;
import burp.utils.OutFormat;
import burp.utils.Utils;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.PBKDF2;

public class PBKDF2IntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final PBKDF2 pbkdf2;
    private final PBKDF2Config config;


    public PBKDF2IntruderPayloadProcessor(final BurpExtender newParent, String extName, PBKDF2Config config) {
        this.parent = newParent;
        this.extName = extName;
        this.config = (config);
        this.pbkdf2 = new PBKDF2(config.Algorithms.name(), config.KeyLength, config.IterationCount);
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - PBKDF2 Encrypt - " + extName;
    }

    private String pbkdf2(byte[] data) {
        byte[] hash = pbkdf2.encrypt(new String(data).toCharArray(), config.Salt);
        return Utils.encode(hash, config.OutFormat);
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {

            byte[] result = pbkdf2(currentPayload).getBytes("UTF-8");
            parent.dict.Log(result, originalPayload);
            return result;
        } catch (Exception e) {
            this.parent.callbacks.issueAlert(e.toString());
            this.parent.stderr.println();
            e.printStackTrace(this.parent.stderr);
            return null;
        }
    }
}
