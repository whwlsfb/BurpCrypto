package burp;

import burp.aes.*;
import burp.execjs.*;
import org.iq80.leveldb.*;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

import java.io.*;

import java.awt.*;
import javax.script.ScriptException;
import javax.swing.*;

public class BurpExtender implements IBurpExtender, ITab {

    public IExtensionHelpers helpers;
    public IBurpExtenderCallbacks callbacks;
    public PrintWriter stdout;
    public PrintWriter stderr;
    public DB store;

    public JPanel aesPanel;
    public AesUtil AesUtil;
    public AesUIHandler AesUI;
    public AesConfig aesConfig = new AesConfig();

    public JSEngine JSEngine;
    public JTabbedPane mainPanel;
    public JPanel rsaPanel;
    public JPanel execJsPanel;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) throws FileNotFoundException, ScriptException, NoSuchMethodException {
        this.callbacks = callbacks;
        this.helpers = callbacks.getHelpers();
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        this.stderr = new PrintWriter(callbacks.getStderr(), true);
        callbacks.setExtensionName("BurpCrypto");
        callbacks.registerExtensionStateListener(new BurpStateListener(this));
        Options options = new Options();
        options.createIfMissing(true);
        try {
            this.store = factory.open(new File("BurpCrypto.ldb"), options);
            callbacks.issueAlert("LevelDb init success!");
        } catch (IOException e) {
            callbacks.issueAlert("LevelDb init failed! error message: " + e.getMessage());
        }
        this.JSEngine = new JSEngine(this);
        stdout.println("BurpCrypto loaded successfully!");
        InitUi();
    }

    private void InitUi() {
        this.AesUI = new AesUIHandler(this);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BurpExtender bthis = BurpExtender.this;
                bthis.mainPanel = new JTabbedPane();
                bthis.aesPanel = AesUI.getPanel();

                bthis.rsaPanel = new JPanel();
                bthis.execJsPanel = new JPanel();
                bthis.mainPanel.addTab("AES", bthis.aesPanel);
                bthis.mainPanel.addTab("RSA", bthis.rsaPanel);
                bthis.mainPanel.addTab("Exec Js", bthis.execJsPanel);
                bthis.callbacks.addSuiteTab(bthis);
            }
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
