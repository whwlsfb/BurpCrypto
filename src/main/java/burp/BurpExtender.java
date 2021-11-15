package burp;

import burp.aes.AesUIHandler;
import burp.des.DesUIHandler;
import burp.execjs.JsUIHandler;
import burp.rsa.RsaUIHandler;
import burp.utils.BurpCryptoMenuFactory;
import burp.utils.BurpStateListener;
import burp.utils.DictLogManager;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static org.iq80.leveldb.impl.Iq80DBFactory.factory;

public class BurpExtender implements IBurpExtender, ITab {

    public IExtensionHelpers helpers;
    public IBurpExtenderCallbacks callbacks;
    public PrintWriter stdout;
    public PrintWriter stderr;
    public DB store;
    public DictLogManager dict;
    public String version = "0.1.9.1";
    public HashMap<String, IIntruderPayloadProcessor> IPProcessors = new HashMap<>();

    public JTabbedPane mainPanel;

    public JPanel aesPanel;
    public AesUIHandler AesUI;


    public JPanel rsaPanel;
    public RsaUIHandler RsaUI;

    public JPanel desPanel;
    public DesUIHandler DesUI;

    public JPanel execJsPanel;
    public JsUIHandler JsUI;

    public boolean RegIPProcessor(String extName, IIntruderPayloadProcessor processor) {
        if (IPProcessors.containsKey(extName)) {
            JOptionPane.showMessageDialog(mainPanel, "This name already exist!");
            return false;
        }
        callbacks.registerIntruderPayloadProcessor(processor);
        IPProcessors.put(extName, processor);
        return true;
    }

    public void RemoveIPProcessor(String extName) {
        if (IPProcessors.containsKey(extName)) {
            IIntruderPayloadProcessor processor = IPProcessors.get(extName);
            callbacks.removeIntruderPayloadProcessor(processor);
            IPProcessors.remove(extName);
        }
    }

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stderr = new PrintWriter(callbacks.getStderr(), true);
        callbacks.setExtensionName("BurpCrypto v" + version);
        callbacks.registerExtensionStateListener(new BurpStateListener(this));
        callbacks.registerContextMenuFactory(new BurpCryptoMenuFactory(this));
        Options options = new Options();
        options.createIfMissing(true);
        try {
            this.store = factory.open(new File("BurpCrypto.ldb"), options);
            this.dict = new DictLogManager(this);
            callbacks.issueAlert("LevelDb init success!");
        } catch (IOException e) {
            callbacks.issueAlert("LevelDb init failed! error message: " + e.getMessage());
        }

        stdout.println("BurpCrypto loaded successfully!\r\n");
        stdout.println("Anthor: Whwlsfb");
        stdout.println("Email: whwlsfb@wanghw.cn");
        stdout.println("Github: https://github.com/whwlsfb/BurpCrypto");
        InitUi();
    }

    private void InitUi() {
        this.AesUI = new AesUIHandler(this);
        this.RsaUI = new RsaUIHandler(this);
        this.JsUI = new JsUIHandler(this);
        this.DesUI = new DesUIHandler(this);
        SwingUtilities.invokeLater(() -> {
            BurpExtender bthis = BurpExtender.this;
            bthis.mainPanel = new JTabbedPane();
            bthis.aesPanel = AesUI.getPanel();
            bthis.mainPanel.addTab("AES", bthis.aesPanel);
            bthis.rsaPanel = RsaUI.getPanel();
            bthis.mainPanel.addTab("RSA", bthis.rsaPanel);
            bthis.desPanel = DesUI.getPanel();
            bthis.mainPanel.addTab("DES", bthis.desPanel);
            bthis.execJsPanel = JsUI.getPanel();
            bthis.mainPanel.addTab("Exec Js", bthis.execJsPanel);
            bthis.callbacks.addSuiteTab(bthis);
        });
    }

    @Override
    public String getTabCaption() {
        return "BurpCrypto";
    }

    @Override
    public Component getUiComponent() {
        return this.mainPanel;
    }
}
