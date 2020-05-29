package burp.execjs;

import burp.BurpExtender;
import burp.rsa.RsaConfig;
import burp.rsa.RsaIntruderPayloadProcessor;
import burp.utils.OutFormat;
import burp.utils.PublicKeyFormat;
import burp.utils.UIUtil;
import burp.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigInteger;

public class JsUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JTextField methodText;
    private JTextArea jsCodeText;
    private JButton applyBtn, deleteBtn;

    public JsUIHandler(BurpExtender parent) {
        this.parent = parent;
    }

    public JPanel getPanel() {
        mainPanel = new JPanel();
        mainPanel.setAlignmentX(0.0f);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, 1));

        final JLabel label1 = new JLabel("Exec Js Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel1 = UIUtil.GetXJPanel();
        final JPanel panel2 = UIUtil.GetXJPanel();
        final JPanel panel3 = UIUtil.GetXJPanel();

        final JLabel label2 = new JLabel("Js Method Name: ");
        methodText = new JTextField(200);
        methodText.setMaximumSize(methodText.getPreferredSize());


        final JLabel label3 = new JLabel("Js Code: ");
        jsCodeText = new JTextArea(5, 10);
        jsCodeText.setLineWrap(true);
        final JScrollPane codePane = new JScrollPane(jsCodeText);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            JsConfig config = new JsConfig();
            config.CryptoJsCode = jsCodeText.getText();
            config.MethodName = methodText.getText();
            String extName = JOptionPane.showInputDialog("Please give this processor a special name:");
            if (extName.length() == 0) {
                JOptionPane.showMessageDialog(mainPanel, "name empty!");
                return;
            }
            if (parent.RegIPProcessor(extName, new ExecJSIntruderPayloadProcessor(parent, extName, config)))
                JOptionPane.showMessageDialog(mainPanel, "Apply processor success!");
        });

        deleteBtn = new JButton("Remove processor");
        deleteBtn.setMaximumSize(deleteBtn.getPreferredSize());
        deleteBtn.addActionListener(e -> {
            String extName = JOptionPane.showInputDialog("Please enter the special name you want to delete:");
            if (extName.length() == 0) {
                JOptionPane.showMessageDialog(mainPanel, "name empty!");
                return;
            }
            parent.RemoveIPProcessor(extName);
            JOptionPane.showMessageDialog(mainPanel, "Remove success!");
        });


        panel3.add(codePane);
        panel1.add(label2);
        panel1.add(methodText);
        panel2.add(applyBtn);
        panel2.add(deleteBtn);


        mainPanel.add(label1);
        mainPanel.add(label3);
        mainPanel.add(panel3);
        mainPanel.add(panel1);
        mainPanel.add(panel2);

        return mainPanel;
    }
}
