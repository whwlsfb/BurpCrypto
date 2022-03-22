package burp.sm4;

import burp.BurpExtender;
import burp.utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class SM4UIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> sm4AlgSelector;
    private JComboBox<String> sm4KeyFormatSelector;
    private JComboBox<String> sm4IVFormatSelector;
    private JComboBox<String> sm4OutFormatSelector;
    private JTextField sm4KeyText;
    private JTextField sm4IVText;
    private JButton applyBtn, deleteBtn;

    public SM4UIHandler(BurpExtender parent) {
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

        final JLabel label1 = new JLabel("SM4 Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel1 = UIUtil.GetXJPanel();
        final JPanel panel2 = UIUtil.GetXJPanel();
        final JPanel panel3 = UIUtil.GetXJPanel();
        final JPanel panel4 = UIUtil.GetXJPanel();
        final JPanel panel5 = UIUtil.GetXJPanel();

        final JLabel label2 = new JLabel("SM4 Alg: ");
        sm4AlgSelector = new JComboBox(GetSM4Algs());
        sm4AlgSelector.setMaximumSize(sm4AlgSelector.getPreferredSize());
        sm4AlgSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                CipherInfo cipherInfo = new CipherInfo(item);
                panel3.setVisible(!cipherInfo.Mode.equals("ECB"));
            }
        });
        sm4AlgSelector.setSelectedIndex(0);

        final JLabel label3 = new JLabel("SM4 Key: ");
        sm4KeyFormatSelector = new JComboBox(Utils.GetKeyFormats());
        sm4KeyFormatSelector.setMaximumSize(sm4KeyFormatSelector.getPreferredSize());
        sm4KeyFormatSelector.setSelectedIndex(0);
        sm4KeyText = new JTextField(200);
        sm4KeyText.setMaximumSize(sm4KeyText.getPreferredSize());

        final JLabel label4 = new JLabel("SM4 IV: ");
        sm4IVFormatSelector = new JComboBox(Utils.GetKeyFormats());
        sm4IVFormatSelector.setMaximumSize(sm4IVFormatSelector.getPreferredSize());
        sm4IVFormatSelector.setSelectedIndex(0);
        sm4IVText = new JTextField(200);
        sm4IVText.setMaximumSize(sm4IVText.getPreferredSize());

        final JLabel label5 = new JLabel("Output Format: ");
        sm4OutFormatSelector = new JComboBox(Utils.GetOutFormats());
        sm4OutFormatSelector.setMaximumSize(sm4OutFormatSelector.getPreferredSize());
        sm4OutFormatSelector.setSelectedIndex(0);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            SM4Algorithms alg = SM4Algorithms.valueOf(sm4AlgSelector.getSelectedItem().toString().replace('/', '_'));
            KeyFormat keyFormat = KeyFormat.valueOf(sm4KeyFormatSelector.getSelectedItem().toString());
            KeyFormat ivFormat = KeyFormat.valueOf(sm4IVFormatSelector.getSelectedItem().toString());
            OutFormat outFormat = OutFormat.valueOf(sm4OutFormatSelector.getSelectedItem().toString());
            SM4Config sm4Config = new SM4Config();
            CipherInfo cipherInfo = new CipherInfo(sm4AlgSelector.getSelectedItem().toString());
            sm4Config.Algorithms = alg;
            sm4Config.OutFormat = outFormat;
            try {
                sm4Config.Key = Utils.StringKeyToByteKey(sm4KeyText.getText(), keyFormat);
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(mainPanel, "Key format error!");
                return;
            }
            if (!cipherInfo.Mode.equals("ECB"))
                try {
                    sm4Config.IV = Utils.StringKeyToByteKey(sm4IVText.getText(), ivFormat);
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
            if (parent.RegIPProcessor(extName, new SM4IntruderPayloadProcessor(parent, extName, sm4Config)))
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
        panel1.add(sm4AlgSelector);
        panel2.add(label3);
        panel2.add(sm4KeyFormatSelector);
        panel2.add(sm4KeyText);
        panel3.add(label4);
        panel3.add(sm4IVFormatSelector);
        panel3.add(sm4IVText);
        panel4.add(label5);
        panel4.add(sm4OutFormatSelector);
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

    private String[] GetSM4Algs() {
        ArrayList<String> algStrs = new ArrayList<String>();
        SM4Algorithms[] algs = SM4Algorithms.values();
        for (SM4Algorithms alg : algs) {
            algStrs.add(alg.name().replace('_', '/'));
        }
        return algStrs.toArray(new String[algStrs.size()]);
    }

}
