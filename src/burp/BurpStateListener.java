package burp;

import java.io.IOException;

public class BurpStateListener implements  IExtensionStateListener {
    private BurpExtender parent;
    public BurpStateListener(final BurpExtender newParent) {
        this.parent = newParent;
    }
    @Override
    public void extensionUnloaded() {
        try {
            this.parent.store.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
