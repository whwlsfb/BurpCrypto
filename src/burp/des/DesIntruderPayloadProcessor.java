package burp.des;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;
import burp.aes.AesConfig;
import burp.aes.AesUtil;

import java.nio.charset.StandardCharsets;

public class DesIntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final DesUtil DesUtil;

    public DesIntruderPayloadProcessor(final BurpExtender newParent, String extName, DesConfig config) {
        this.parent = newParent;
        this.extName = extName;
        DesUtil = new DesUtil();
        DesUtil.setConfig(config);
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - DES Encrypt - " + extName;
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {
            byte[] result = DesUtil.encrypt(currentPayload).getBytes(StandardCharsets.UTF_8);
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
