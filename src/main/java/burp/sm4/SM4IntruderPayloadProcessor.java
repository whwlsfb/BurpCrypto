package burp.sm4;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;

public class SM4IntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final SM4Util SM4Util;

    public SM4IntruderPayloadProcessor(final BurpExtender newParent, String extName, SM4Config config) {
        this.parent = newParent;
        this.extName = extName;
        SM4Util = new SM4Util();
        SM4Util.setConfig(config);
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - SM4 Encrypt - " + extName;
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {
            byte[] result = SM4Util.encrypt(currentPayload).getBytes("UTF-8");
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
