package burp.execjs;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;

import java.nio.charset.StandardCharsets;

public class ExecJSIntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final JsUtil2 JsUtil;

    public ExecJSIntruderPayloadProcessor(final BurpExtender newParent, String extName, JsConfig config) {
        this.parent = newParent;
        this.extName = extName;
        this.JsUtil = new JsUtil2();
        this.JsUtil.parent = parent;
        try {
            this.JsUtil.setConfig(config);
        } catch (Exception e) {
            this.parent.callbacks.issueAlert(e.toString());
            this.parent.stderr.println();
            e.printStackTrace(this.parent.stderr);
        }
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - Exec JS - " + extName;
    }

    @Override
    public byte[] processPayload(byte[] currentPayload, byte[] originalPayload, byte[] baseValue) {
        try {
            byte[] result = JsUtil.eval(new String(currentPayload, StandardCharsets.UTF_8)).getBytes("UTF-8");
            parent.dict.Log(result, originalPayload);
            return result;
        } catch (Exception e) {
            this.parent.callbacks.issueAlert(e.getMessage());
            this.parent.stderr.println();
            e.printStackTrace(this.parent.stderr);
            return null;
        }
    }
}