package burp.sm3;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;
import burp.utils.OutFormat;
import burp.utils.Utils;
import cn.hutool.crypto.digest.SM3;

public class SM3IntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final SM3 sm3Utils;
    private final SM3Config config;


    public SM3IntruderPayloadProcessor(final BurpExtender newParent, String extName, SM3Config config) {
        this.parent = newParent;
        this.extName = extName;
        this.config = (config);
        if (config.Salt != null)
            this.sm3Utils = new SM3(config.Salt);
        else this.sm3Utils = new SM3();
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - SM3 Encrypt - " + extName;
    }

    private String SM3Digest(byte[] data) {
        byte[] hash = sm3Utils.digest(data);
        return config.OutFormat == OutFormat.Base64 ? Utils.base64(hash) : Utils.hex(hash);
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {

            byte[] result = SM3Digest(currentPayload).getBytes("UTF-8");
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
