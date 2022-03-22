package burp.zuc;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;

public class ZUCIntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final ZUCUtil ZUCUtil;

    public ZUCIntruderPayloadProcessor(final BurpExtender newParent, String extName, ZUCConfig config) {
        this.parent = newParent;
        this.extName = extName;
        ZUCUtil = new ZUCUtil();
        ZUCUtil.setConfig(config);
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - ZUC Encrypt - " + extName;
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {
            byte[] result = ZUCUtil.encrypt(currentPayload).getBytes("UTF-8");
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
