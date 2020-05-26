package burp;

public class AESIntruderPayloadProcessor implements IIntruderPayloadProcessor
{
    private BurpExtender parent;
    private int proc_type;

    public AESIntruderPayloadProcessor(final BurpExtender newParent, final int type) {
        this.parent = newParent;
        this.proc_type = type;
    }

    @Override
    public String getProcessorName() {
        if (this.proc_type == 0) {
            return "AES Decrypt";
        }
        return "AES Encrypt";
    }

    @Override
    public byte[] processPayload(final byte[] currentPayload, final byte[] originalPayload, final byte[] baseValue) {
        try {
            final String payloadString = new String(currentPayload);
            String result = "";
            if (this.proc_type == 0) {
                //result = this.parent.decrypt(payloadString);
            } else {
                //result = this.parent.encrypt(payloadString);
            }
            return result.getBytes();
        } catch (Exception e) {
            this.parent.callbacks.issueAlert(e.toString());
            return null;
        }
    }
}
