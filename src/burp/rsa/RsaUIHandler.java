package burp.rsa;

import burp.BurpExtender;
import burp.aes.AesAlgorithms;
import burp.aes.AesConfig;
import burp.aes.AesIntruderPayloadProcessor;
import burp.utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.math.BigInteger;

public class RsaUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> rsaPublicKeyFormatSelector, outFormatSelector;
    private JTextField modulusText, exponentText, x509Text;
    private JButton applyBtn, deleteBtn;

    public RsaUIHandler(BurpExtender parent) {
        this.parent = parent;
    }

    public JPanel getPanel() {
        final JSeparator separator = new JSeparator(0);
        separator.setMaximumSize(new Dimension(separator.getMaximumSize().width, separator.getPreferredSize().height));
        separator.setAlignmentX(0.0f);

        mainPanel = new JPanel();
        mainPanel.setAlignmentX(0.0f);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, 1));

        final JLabel label1 = new JLabel("RSA Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel1 = UIUtil.GetXJPanel();
        final JPanel panel2 = UIUtil.GetYJPanel();
        final JPanel panel2_1 = UIUtil.GetXJPanel();
        final JPanel panel2_2 = UIUtil.GetXJPanel();
        final JPanel panel3 = UIUtil.GetXJPanel();
        final JPanel panel4 = UIUtil.GetXJPanel();
        final JPanel panel5 = UIUtil.GetXJPanel();

        final JLabel label2 = new JLabel("RSA Public Key Format: ");
        rsaPublicKeyFormatSelector = new JComboBox(Utils.GetPublicKeyFormats());
        rsaPublicKeyFormatSelector.setMaximumSize(rsaPublicKeyFormatSelector.getPreferredSize());
        rsaPublicKeyFormatSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                PublicKeyFormat format = PublicKeyFormat.valueOf(item);
                switch (format) {
                    case X509:
                        panel2.setVisible(false);
                        panel3.setVisible(true);
                        break;
                    case ModulusAndExponent:
                        panel2.setVisible(true);
                        panel3.setVisible(false);
                        break;
                }
            }
        });
        rsaPublicKeyFormatSelector.setSelectedIndex(0);

        final JLabel label3 = new JLabel("Modulus(HEX): ");
        modulusText = new JTextField(200);
        modulusText.setMaximumSize(modulusText.getPreferredSize());

        final JLabel label4 = new JLabel("Exponent(HEX): ");
        exponentText = new JTextField(200);
        exponentText.setMaximumSize(exponentText.getPreferredSize());

        final JLabel label5 = new JLabel("X509 Key(Base64): ");
        x509Text = new JTextField(200);
        x509Text.setMaximumSize(x509Text.getPreferredSize());

        final JLabel label6 = new JLabel("Output Format: ");
        outFormatSelector = new JComboBox(Utils.GetOutFormats());
        outFormatSelector.setMaximumSize(outFormatSelector.getPreferredSize());
        outFormatSelector.setSelectedIndex(0);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            PublicKeyFormat keyFormat = PublicKeyFormat.valueOf(rsaPublicKeyFormatSelector.getSelectedItem().toString());
            OutFormat outFormat = OutFormat.valueOf(outFormatSelector.getSelectedItem().toString());
            RsaConfig config = new RsaConfig();
            config.OutFormat = outFormat;
            switch (keyFormat) {
                case ModulusAndExponent:
                    try {
                        config.Modulus = new BigInteger(modulusText.getText(), 16);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Modulus error!");
                        return;
                    }
                    try {
                        config.Exponent = new BigInteger(exponentText.getText(), 16);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Exponent error!");
                        return;
                    }
                    break;
                case X509:
                    BigInteger[] keys;
                    try {
                        keys = Utils.getBase64PublicKeyME(x509Text.getText());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainPanel, "X509 error!");
                        return;
                    }
                    config.Modulus = keys[0];
                    config.Exponent = keys[1];
                    break;
            }
            String extName = JOptionPane.showInputDialog("Please give this processor a special name:");
            if (extName.length() == 0) {
                JOptionPane.showMessageDialog(mainPanel, "name empty!");
                return;
            }
            if (parent.RegIPProcessor(extName, new RsaIntruderPayloadProcessor(parent, extName, config)))
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

        panel1.add(label2);
        panel1.add(rsaPublicKeyFormatSelector);
        panel2_1.add(label3);
        panel2_1.add(modulusText);
        panel2_2.add(label4);
        panel2_2.add(exponentText);
        panel2.add(panel2_1);
        panel2.add(panel2_2);
        panel2.setVisible(false);
        panel3.add(label5);
        panel3.add(x509Text);
        panel4.add(label6);
        panel4.add(outFormatSelector);
        panel5.add(applyBtn);
        panel5.add(deleteBtn);

        mainPanel.add(label1);
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);
        mainPanel.add(panel5);
        return mainPanel;
    }
}
