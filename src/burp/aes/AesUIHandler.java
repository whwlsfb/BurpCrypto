package burp.aes;

import burp.BurpExtender;
import burp.utils.*;
import org.checkerframework.checker.nullness.qual.KeyFor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class AesUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> aesAlgSelecter;
    private JComboBox<String> aesKeyFormatSelecter;
    private JComboBox<String> aesIVFormatSelecter;
    private JComboBox<String> aesOutFormatSelecter;
    private JTextField aesKeyText;
    private JTextField aesIVText;
    private JButton applyBtn, deleteBtn;

    public AesUIHandler(BurpExtender parent) {
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

        final JLabel label1 = new JLabel("AES Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel1 = UIUtil.GetXJPanel();
        final JPanel panel2 = UIUtil.GetXJPanel();
        final JPanel panel3 = UIUtil.GetXJPanel();
        final JPanel panel4 = UIUtil.GetXJPanel();
        final JPanel panel5 = UIUtil.GetXJPanel();

        final JLabel label2 = new JLabel("AES Alg: ");
        aesAlgSelecter = new JComboBox(GetAesAlgs());
        aesAlgSelecter.setMaximumSize(aesAlgSelecter.getPreferredSize());
        aesAlgSelecter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                panel3.setVisible(!item.startsWith("AES/ECB/"));
            }
        });
        aesAlgSelecter.setSelectedIndex(0);

        final JLabel label3 = new JLabel("AES Key: ");
        aesKeyFormatSelecter = new JComboBox(GetKeyFormats());
        aesKeyFormatSelecter.setMaximumSize(aesKeyFormatSelecter.getPreferredSize());
        aesKeyFormatSelecter.setSelectedIndex(0);
        aesKeyText = new JTextField(200);
        aesKeyText.setMaximumSize(aesKeyText.getPreferredSize());

        final JLabel label4 = new JLabel("AES IV: ");
        aesIVFormatSelecter = new JComboBox(GetKeyFormats());
        aesIVFormatSelecter.setMaximumSize(aesIVFormatSelecter.getPreferredSize());
        aesIVFormatSelecter.setSelectedIndex(0);
        aesIVText = new JTextField(200);
        aesIVText.setMaximumSize(aesIVText.getPreferredSize());

        final JLabel label5 = new JLabel("Output Format: ");
        aesOutFormatSelecter = new JComboBox(GetOutFormats());
        aesOutFormatSelecter.setMaximumSize(aesOutFormatSelecter.getPreferredSize());
        aesOutFormatSelecter.setSelectedIndex(0);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            AesAlgorithms alg = AesAlgorithms.valueOf(aesAlgSelecter.getSelectedItem().toString().replace('/', '_'));
            KeyFormat keyFormat = KeyFormat.valueOf(aesKeyFormatSelecter.getSelectedItem().toString());
            KeyFormat ivFormat = KeyFormat.valueOf(aesIVFormatSelecter.getSelectedItem().toString());
            OutFormat outFormat = OutFormat.valueOf(aesOutFormatSelecter.getSelectedItem().toString());
            parent.AesConfig = new AesConfig();
            parent.AesConfig.Algorithms = alg;
            parent.AesConfig.OutFormat = outFormat;
            try {
                parent.AesConfig.Key = Utils.StringKeyToByteKey(aesKeyText.getText(), keyFormat);
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(mainPanel, "Key format error!");
                return;
            }
            if (!alg.name().startsWith("AES_ECB_"))
                try {
                    parent.AesConfig.IV = Utils.StringKeyToByteKey(aesIVText.getText(), ivFormat);
                } catch (Exception ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(mainPanel, "IV format error!");
                    return;
                }
            String extName = JOptionPane.showInputDialog("Please give this setting a special name:");
            if (extName.length() == 0) {
                JOptionPane.showMessageDialog(mainPanel, "name empty!");
                return;
            }
            if (parent.RegIPProcessor(extName, new AesIntruderPayloadProcessor(parent, extName, parent.AesConfig)))
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

        final JLabel label6 = new JLabel("Encrypt Test");
        label6.setForeground(new Color(249, 130, 11));
        label6.setFont(new Font("Nimbus", 1, 16));
        label6.setAlignmentX(0.0f);

        panel1.add(label2);
        panel1.add(aesAlgSelecter);
        panel2.add(label3);
        panel2.add(aesKeyFormatSelecter);
        panel2.add(aesKeyText);
        panel3.add(label4);
        panel3.add(aesIVFormatSelecter);
        panel3.add(aesIVText);
        panel4.add(label5);
        panel4.add(aesOutFormatSelecter);
        panel5.add(applyBtn);
        panel5.add(deleteBtn);

        mainPanel.add(label1);
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);
        mainPanel.add(panel5);
        mainPanel.add(separator);
        mainPanel.add(label6);
        return mainPanel;
    }

    private String[] GetAesAlgs() {
        ArrayList<String> algStrs = new ArrayList<String>();
        AesAlgorithms[] algs = AesAlgorithms.values();
        for (AesAlgorithms alg : algs) {
            algStrs.add(alg.name().replace('_', '/'));
        }
        return algStrs.toArray(new String[algStrs.size()]);
    }

    private String[] GetKeyFormats() {
        ArrayList<String> strs = new ArrayList<String>();
        KeyFormat[] items = KeyFormat.values();
        for (KeyFormat item : items) {
            strs.add(item.name());
        }
        return strs.toArray(new String[strs.size()]);
    }

    private String[] GetOutFormats() {
        ArrayList<String> strs = new ArrayList<String>();
        OutFormat[] items = OutFormat.values();
        for (OutFormat item : items) {
            strs.add(item.name());
        }
        return strs.toArray(new String[strs.size()]);
    }
}
