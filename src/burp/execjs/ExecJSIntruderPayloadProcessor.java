package burp.execjs;

import burp.IIntruderPayloadProcessor;

public class ExecJSIntruderPayloadProcessor implements IIntruderPayloadProcessor {

    @Override
    public String getProcessorName() {
        return "BurpCrypto - Exec JS";
    }

    @Override
    public byte[] processPayload(byte[] currentPayload, byte[] originalPayload, byte[] baseValue) {
        return new byte[0];
    }
}
