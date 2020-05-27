package burp.aes;

import burp.BurpExtender;
import burp.IIntruderPayloadProcessor;

public class AesIntruderPayloadProcessor implements IIntruderPayloadProcessor
{
    private BurpExtender parent;

    public AesIntruderPayloadProcessor(final BurpExtender newParent) {
        this.parent = newParent;
    }

    @Override
    public String getProcessorName() {
        return "BurpCrypto - AES Encrypt";
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {
            final String payloadString = new String(currentPayload);
            String result = "";

            return result.getBytes();
        } catch (Exception e) {
            this.parent.callbacks.issueAlert(e.toString());
            return null;
        }
    }
}
