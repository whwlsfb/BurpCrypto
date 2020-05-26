package burp;

import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;

import java.awt.*;
import java.util.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.spec.*;
import java.security.*;
import java.util.List;

public class BurpExtender implements IBurpExtender, IScannerInsertionPointProvider, ITab {

    public IExtensionHelpers helpers;
    public IBurpExtenderCallbacks callbacks;
    public DB store;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        callbacks.setExtensionName("BurpCrypto");
        callbacks.registerHttpListener(new OriginalPayloadHttpListener(this));
        callbacks.registerExtensionStateListener(new BurpStateListener(this));
        Options options = new Options();
        options.createIfMissing(true);
        try {
            this.store = factory.open(new File("BurpCrypto.db"), options);
            callbacks.issueAlert("LevelDb init success!");
        }catch (IOException e) {
            callbacks.issueAlert(e.getMessage());
        }
    }

    @Override
    public List<IScannerInsertionPoint> getInsertionPoints(IHttpRequestResponse baseRequestResponse) {

        return null;
    }

    @Override
    public String getTabCaption() {
        return null;
    }

    @Override
    public Component getUiComponent() {
        return null;
    }
}
