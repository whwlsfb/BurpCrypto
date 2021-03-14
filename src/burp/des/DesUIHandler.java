package burp.des;

import burp.BurpExtender;
import burp.utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class DesUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> desAlgSelector;
    private JComboBox<String> desKeyFormatSelector;
    private JComboBox<String> desIVFormatSelector;
    private JComboBox<String> desOutFormatSelector;
    private JTextField desKeyText;
    private JTextField desIVText;
    private JTextField desEncKey1Text;
    private JTextField desEncKey2Text;
    private JTextField desEncKey3Text;
    private JButton applyBtn, deleteBtn;

    public DesUIHandler(BurpExtender parent) {
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

        final JLabel label1 = new JLabel("DES Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel1 = UIUtil.GetXJPanel();
        final JPanel panel2 = UIUtil.GetXJPanel();
        final JPanel panel3 = UIUtil.GetXJPanel();
        final JPanel panel4 = UIUtil.GetXJPanel();
        final JPanel panel5 = UIUtil.GetXJPanel();
        final JPanel panel6 = UIUtil.GetXJPanel();
        final JPanel panel7 = UIUtil.GetXJPanel();
        final JPanel panel8 = UIUtil.GetXJPanel();

        final JLabel label2 = new JLabel("DES Alg: ");
        desAlgSelector = new JComboBox(GetDesAlgs());
        desAlgSelector.setMaximumSize(desAlgSelector.getPreferredSize());
        desAlgSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                if (item.equals("strEnc")) {
                    panel2.setVisible(false);
                    panel3.setVisible(false);
                    panel4.setVisible(true);
                    panel5.setVisible(true);
                    panel6.setVisible(true);
                    panel7.setVisible(false);
                } else {
                    CipherInfo cipherInfo = new CipherInfo(item);
                    panel2.setVisible(true);
                    panel3.setVisible(!cipherInfo.Mode.equals("ECB"));
                    panel4.setVisible(false);
                    panel5.setVisible(false);
                    panel6.setVisible(false);
                    panel7.setVisible(true);
                }
            }
        });
        desAlgSelector.setSelectedIndex(0);

        final JLabel label3 = new JLabel("DES Key: ");
        desKeyFormatSelector = new JComboBox(GetKeyFormats());
        desKeyFormatSelector.setMaximumSize(desKeyFormatSelector.getPreferredSize());
        desKeyFormatSelector.setSelectedIndex(0);
        desKeyText = new JTextField(200);
        desKeyText.setMaximumSize(desKeyText.getPreferredSize());

        final JLabel label4 = new JLabel("DES IV: ");
        desIVFormatSelector = new JComboBox(GetKeyFormats());
        desIVFormatSelector.setMaximumSize(desIVFormatSelector.getPreferredSize());
        desIVFormatSelector.setSelectedIndex(0);
        desIVText = new JTextField(200);
        desIVText.setMaximumSize(desIVText.getPreferredSize());

        final JLabel label5 = new JLabel("DES strEnc Key1: ");
        desEncKey1Text = new JTextField(200);
        desEncKey1Text.setMaximumSize(desEncKey1Text.getPreferredSize());

        final JLabel label6 = new JLabel("DES strEnc Key2: ");
        desEncKey2Text = new JTextField(200);
        desEncKey2Text.setMaximumSize(desEncKey2Text.getPreferredSize());

        final JLabel label7 = new JLabel("DES strEnc Key3: ");
        desEncKey3Text = new JTextField(200);
        desEncKey3Text.setMaximumSize(desEncKey3Text.getPreferredSize());

        final JLabel label8 = new JLabel("Output Format: ");
        desOutFormatSelector = new JComboBox(Utils.GetOutFormats());
        desOutFormatSelector.setMaximumSize(desOutFormatSelector.getPreferredSize());
        desOutFormatSelector.setSelectedIndex(0);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            DesAlgorithms alg = DesAlgorithms.valueOf(desAlgSelector.getSelectedItem().toString().replace('/', '_'));
            KeyFormat keyFormat = KeyFormat.valueOf(desKeyFormatSelector.getSelectedItem().toString());
            KeyFormat ivFormat = KeyFormat.valueOf(desIVFormatSelector.getSelectedItem().toString());
            OutFormat outFormat = OutFormat.valueOf(desOutFormatSelector.getSelectedItem().toString());
            DesConfig desConfig = new DesConfig();
            desConfig.Algorithms = alg;
            desConfig.OutFormat = outFormat;
            if (alg == DesAlgorithms.strEnc) {
                desConfig.Key1 = desEncKey1Text.getText();
                if (desConfig.Key1 == null) desConfig.Key1 = "";
                desConfig.Key2 = desEncKey2Text.getText();
                if (desConfig.Key2 == null) desConfig.Key2 = "";
                desConfig.Key3 = desEncKey3Text.getText();
                if (desConfig.Key3 == null) desConfig.Key3 = "";
            } else {
                CipherInfo cipherInfo = new CipherInfo(desAlgSelector.getSelectedItem().toString());
                try {
                    desConfig.Key = Utils.StringKeyToByteKey(desKeyText.getText(), keyFormat);
                } catch (Exception ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(mainPanel, "Key format error!");
                    return;
                }
                if (!cipherInfo.Mode.equals("ECB"))
                    try {
                        desConfig.IV = Utils.StringKeyToByteKey(desIVText.getText(), ivFormat);
                    } catch (Exception ex) {
                        System.out.println(ex);
                        JOptionPane.showMessageDialog(mainPanel, "IV format error!");
                        return;
                    }
            }
            try {
                new DesIntruderPayloadProcessor(parent, "Test", desConfig);
            } catch(Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(mainPanel, "key spec error!");
                return;
            }
            String extName = JOptionPane.showInputDialog("Please give this processor a special name:");
            if (extName != null) {
                if (extName.length() == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "name empty!");
                    return;
                }
            } else return;
            if (parent.RegIPProcessor(extName, new DesIntruderPayloadProcessor(parent, extName, desConfig)))
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
        panel1.add(desAlgSelector);
        panel2.add(label3);
        panel2.add(desKeyFormatSelector);
        panel2.add(desKeyText);
        panel3.add(label4);
        panel3.add(desIVFormatSelector);
        panel3.add(desIVText);
        panel4.add(label5);
        panel4.add(desEncKey1Text);
        panel5.add(label6);
        panel5.add(desEncKey2Text);
        panel6.add(label7);
        panel6.add(desEncKey3Text);
        panel7.add(label8);
        panel7.add(desOutFormatSelector);
        panel8.add(applyBtn);
        panel8.add(deleteBtn);

        panel4.setVisible(false);
        panel5.setVisible(false);
        panel6.setVisible(false);

        mainPanel.add(label1);
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(panel4);
        mainPanel.add(panel5);
        mainPanel.add(panel6);
        mainPanel.add(panel7);
        mainPanel.add(panel8);
        return mainPanel;
    }

    private String[] GetDesAlgs() {
        ArrayList<String> algStrs = new ArrayList<String>();
        DesAlgorithms[] algs = DesAlgorithms.values();
        for (DesAlgorithms alg : algs) {
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
}
