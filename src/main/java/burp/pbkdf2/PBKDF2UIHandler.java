package burp.pbkdf2;

import burp.BurpExtender;
import burp.utils.KeyFormat;
import burp.utils.OutFormat;
import burp.utils.UIUtil;
import burp.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class PBKDF2UIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> algSelector;
    private JComboBox<String> saltFormatSelector;
    private JComboBox<String> outFormatSelector;
    private JTextField keyLengthText, iterationCountText, saltText;
    private JButton applyBtn, deleteBtn;

    public PBKDF2UIHandler(BurpExtender parent) {
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

        final JLabel label1 = new JLabel("PBKDF2 Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel0 = UIUtil.GetXJPanel();
        final JPanel panel1 = UIUtil.GetXJPanel();
        final JPanel panel2 = UIUtil.GetXJPanel();
        final JPanel panel3 = UIUtil.GetXJPanel();
        final JPanel panel4 = UIUtil.GetXJPanel();
        final JPanel panel5 = UIUtil.GetXJPanel();

        final JLabel label2_1 = new JLabel("Key Length: ");
        keyLengthText = new JTextField(200);
        keyLengthText.setMaximumSize(keyLengthText.getPreferredSize());
        keyLengthText.setText("512");

        final JLabel label2_2 = new JLabel("Iteration Count: ");
        iterationCountText = new JTextField(200);
        iterationCountText.setMaximumSize(iterationCountText.getPreferredSize());
        iterationCountText.setText("1000");

        final JLabel label2_3 = new JLabel("Mode: ");
        algSelector = new JComboBox(GetPBKDF2Modes());
        algSelector.setMaximumSize(algSelector.getPreferredSize());
        algSelector.setSelectedIndex(0);

        final JLabel label3 = new JLabel("Salt: ");
        saltFormatSelector = new JComboBox(Utils.GetKeyFormats());
        saltFormatSelector.setMaximumSize(saltFormatSelector.getPreferredSize());
        saltFormatSelector.setSelectedIndex(0);
        saltText = new JTextField(200);
        saltText.setMaximumSize(saltText.getPreferredSize());

        final JLabel label5 = new JLabel("Output Format: ");
        outFormatSelector = new JComboBox(Utils.GetOutFormats());
        outFormatSelector.setMaximumSize(outFormatSelector.getPreferredSize());
        outFormatSelector.setSelectedIndex(0);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            PBKDF2Config config = new PBKDF2Config();
            KeyFormat saltFormat = KeyFormat.valueOf(saltFormatSelector.getSelectedItem().toString());
            config.Algorithms = PBKDF2Algorithms.valueOf(algSelector.getSelectedItem().toString());
            config.OutFormat = OutFormat.valueOf(outFormatSelector.getSelectedItem().toString());
            try {
                config.KeyLength = Integer.valueOf(keyLengthText.getText());
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(mainPanel, "KeyLength format error!");
                return;
            }
            try {
                config.IterationCount = Integer.valueOf(iterationCountText.getText());
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(mainPanel, "IterationCount format error!");
                return;
            }
            try {
                String salt = saltText.getText();
                if (!"".equals(salt) && salt != null) {
                    config.Salt = Utils.StringKeyToByteKey(saltText.getText(), saltFormat);
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Salt must be non-null!");
                    return;
                }
            } catch (Exception ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(mainPanel, "Salt format error!");
                return;
            }
            String extName = JOptionPane.showInputDialog("Please give this processor a special name:");
            if (extName != null) {
                if (extName.length() == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "name empty!");
                    return;
                }
            } else return;
            if (parent.RegIPProcessor(extName, new PBKDF2IntruderPayloadProcessor(parent, extName, config)))
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


        panel0.add(label2_1);
        panel0.add(keyLengthText);
        panel1.add(label2_2);
        panel1.add(iterationCountText);
        panel2.add(label2_3);
        panel2.add(algSelector);
        panel3.add(label3);
        panel3.add(saltFormatSelector);
        panel3.add(saltText);
        panel4.add(label5);
        panel4.add(outFormatSelector);
        panel5.add(applyBtn);
        panel5.add(deleteBtn);

        mainPanel.add(label1);

        mainPanel.add(panel2);
        mainPanel.add(panel0);
        mainPanel.add(panel1);
        mainPanel.add(panel3);
        mainPanel.add(panel4);
        mainPanel.add(panel5);
        return mainPanel;
    }

    private String[] GetPBKDF2Modes() {
        ArrayList<String> algStrs = new ArrayList<String>();
        PBKDF2Algorithms[] algs = PBKDF2Algorithms.values();
        for (PBKDF2Algorithms alg : algs) {
            algStrs.add(alg.name().replace('_', '/'));
        }
        return algStrs.toArray(new String[algStrs.size()]);
    }
}
