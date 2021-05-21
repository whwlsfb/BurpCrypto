package burp.execjs;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;
import burp.execjs.engine.HtmlUnitEngine;
import burp.execjs.engine.JreBuiltInEngine;
import burp.execjs.engine.RhinoEngine;

import java.nio.charset.StandardCharsets;

public class ExecJSIntruderPayloadProcessor implements IIntruderPayloadProcessor {
    private BurpExtender parent;
    private final String extName;
    private final IJsEngine jsEngine;

    public ExecJSIntruderPayloadProcessor(final BurpExtender newParent, String extName, JsConfig config) {
        this.parent = newParent;
        this.extName = extName;
        switch (config.JsEngine){
            case HtmlUnit:
                this.jsEngine = new HtmlUnitEngine();
                break;
            case JreBuiltIn:
                this.jsEngine = new JreBuiltInEngine();
                break;
            default:
                this.jsEngine = new RhinoEngine();
        }
        this.jsEngine.setParent(parent);
        try {
            this.jsEngine.setConfig(config);
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
            byte[] result = jsEngine.eval(new String(currentPayload, StandardCharsets.UTF_8)).getBytes("UTF-8");
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
