package burp;

public class OriginalPayloadHttpListener implements IHttpListener {

    private BurpExtender parent;

    public OriginalPayloadHttpListener(final BurpExtender newParent) {
        this.parent = newParent;
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        if (!messageIsRequest && parent.callbacks.TOOL_INTRUDER == toolFlag) {
            messageInfo.setComment("test");
        }
    }
}
