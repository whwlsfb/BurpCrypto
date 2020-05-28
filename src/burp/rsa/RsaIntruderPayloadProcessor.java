package burp.rsa;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;
import burp.aes.AesConfig;
import burp.aes.AesUtil;
import burp.utils.EncryptionType;

public class RsaIntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final RsaUtil RsaUtil;

    public RsaIntruderPayloadProcessor(final BurpExtender newParent, String extName, RsaConfig config) {
        this.parent = newParent;
        this.extName = extName;
        RsaUtil = new RsaUtil();
        RsaUtil.setConfig(config);
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - RSA Encrypt - " + extName;
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {
            byte[] result = RsaUtil.encrypt(currentPayload).getBytes("UTF-8");
            parent.dict.Log(result, originalPayload);
            return result;
        } catch (Exception e) {
            this.parent.callbacks.issueAlert(e.toString());
            return null;
        }
    }
}
