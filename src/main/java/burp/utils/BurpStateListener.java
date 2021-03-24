package burp.utils;

import burp.BurpExtender;
import burp.IExtensionStateListener;

import java.io.IOException;

public class BurpStateListener implements IExtensionStateListener {
    private BurpExtender parent;
    public BurpStateListener(final BurpExtender newParent) {
        this.parent = newParent;
    }
    @Override
    public void extensionUnloaded() {
        try {
            this.parent.store.close();  // leveldb unload.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
