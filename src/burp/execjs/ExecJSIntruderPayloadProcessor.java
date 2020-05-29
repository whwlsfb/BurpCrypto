package burp.execjs;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;

public class ExecJSIntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;

    public ExecJSIntruderPayloadProcessor(final BurpExtender newParent, String extName, JsConfig config) {
        this.parent = newParent;
        this.extName = extName;
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - Exec JS";
    }

    @Override
    public byte[] processPayload(byte[] currentPayload, byte[] originalPayload, byte[] baseValue) {
        return new byte[0];
    }
}
