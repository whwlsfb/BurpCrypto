package burp.zuc;

import burp.BurpExtender;
import burp.utils.*;
import cn.hutool.crypto.symmetric.ZUC;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class ZUCUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> zucAlgSelector;
    private JComboBox<String> zucKeyFormatSelector;
    private JComboBox<String> zucIVFormatSelector;
    private JComboBox<String> zucOutFormatSelector;
    private JTextField zucKeyText;
    private JTextField zucIVText;
    private JButton applyBtn, deleteBtn;

    public ZUCUIHandler(BurpExtender parent) {
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

        final JLabel label1 = new JLabel("ZUC Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel1 = UIUtil.GetXJPanel();
        final JPanel panel2 = UIUtil.GetXJPanel();
        final JPanel panel3 = UIUtil.GetXJPanel();
        final JPanel panel4 = UIUtil.GetXJPanel();
        final JPanel panel5 = UIUtil.GetXJPanel();

        final JLabel label2 = new JLabel("ZUC Alg: ");
        zucAlgSelector = new JComboBox(GetZUCAlgs());
        zucAlgSelector.setMaximumSize(zucAlgSelector.getPreferredSize());
        zucAlgSelector.setSelectedIndex(0);

        final JLabel label3 = new JLabel("ZUC Key: ");
        zucKeyFormatSelector = new JComboBox(Utils.GetKeyFormats());
        zucKeyFormatSelector.setMaximumSize(zucKeyFormatSelector.getPreferredSize());
        zucKeyFormatSelector.setSelectedIndex(0);
        zucKeyText = new JTextField(200);
        zucKeyText.setMaximumSize(zucKeyText.getPreferredSize());

        final JLabel label4 = new JLabel("ZUC IV: ");
        zucIVFormatSelector = new JComboBox(Utils.GetKeyFormats());
        zucIVFormatSelector.setMaximumSize(zucIVFormatSelector.getPreferredSize());
        zucIVFormatSelector.setSelectedIndex(0);
        zucIVText = new JTextField(200);
        zucIVText.setMaximumSize(zucIVText.getPreferredSize());

        final JLabel label5 = new JLabel("Output Format: ");
        zucOutFormatSelector = new JComboBox(Utils.GetOutFormats());
        zucOutFormatSelector.setMaximumSize(zucOutFormatSelector.getPreferredSize());
        zucOutFormatSelector.setSelectedIndex(0);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            ZUC.ZUCAlgorithm alg = ZUC.ZUCAlgorithm.valueOf(zucAlgSelector.getSelectedItem().toString());
            KeyFormat keyFormat = KeyFormat.valueOf(zucKeyFormatSelector.getSelectedItem().toString());
            KeyFormat ivFormat = KeyFormat.valueOf(zucIVFormatSelector.getSelectedItem().toString());
            OutFormat outFormat = OutFormat.valueOf(zucOutFormatSelector.getSelectedItem().toString());
            ZUCConfig zucConfig = new ZUCConfig();
            zucConfig.Algorithms = alg;
            zucConfig.OutFormat = outFormat;
            try {
                zucConfig.Key = Utils.StringKeyToByteKey(zucKeyText.getText(), keyFormat);
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(mainPanel, "Key format error!");
                return;
            }
            String ivText = zucIVText.getText();
            if (!"".equals(ivText) && ivText != null)
                try {
                    zucConfig.IV = Utils.StringKeyToByteKey(zucIVText.getText(), ivFormat);
                } catch (Exception ex) {
                    System.out.println(ex);
                    JOptionPane.showMessageDialog(mainPanel, "IV format error!");
                    return;
                }
            String extName = JOptionPane.showInputDialog("Please give this processor a special name:");
            if (extName != null) {
                if (extName.length() == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "name empty!");
                    return;
                }
            } else return;
            if (parent.RegIPProcessor(extName, new ZUCIntruderPayloadProcessor(parent, extName, zucConfig)))
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
        panel1.add(zucAlgSelector);
        panel2.add(label3);
        panel2.add(zucKeyFormatSelector);
        panel2.add(zucKeyText);
        panel3.add(label4);
        panel3.add(zucIVFormatSelector);
        panel3.add(zucIVText);
        panel4.add(label5);
        panel4.add(zucOutFormatSelector);
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

    private String[] GetZUCAlgs() {
        ArrayList<String> algStrs = new ArrayList<String>();
        ZUC.ZUCAlgorithm[] algs = ZUC.ZUCAlgorithm.values();
        for (ZUC.ZUCAlgorithm alg : algs) {
            algStrs.add(alg.name());
        }
        return algStrs.toArray(new String[algStrs.size()]);
    }

}
