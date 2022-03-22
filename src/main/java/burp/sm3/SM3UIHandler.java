package burp.sm3;

import burp.BurpExtender;
import burp.utils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SM3UIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> outFormatSelector;
    private JButton applyBtn, deleteBtn;

    public SM3UIHandler(BurpExtender parent) {
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

        final JLabel label1 = new JLabel("SM3 Setting");
        label1.setForeground(new Color(249, 130, 11));
        label1.setFont(new Font("Nimbus", 1, 16));
        label1.setAlignmentX(0.0f);

        final JPanel panel4 = UIUtil.GetXJPanel();
        final JPanel panel5 = UIUtil.GetXJPanel();

        final JLabel label5 = new JLabel("Output Format: ");
        outFormatSelector = new JComboBox(Utils.GetOutFormats());
        outFormatSelector.setMaximumSize(outFormatSelector.getPreferredSize());
        outFormatSelector.setSelectedIndex(0);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            SM3Config config = new SM3Config();
            config.OutFormat = OutFormat.valueOf(outFormatSelector.getSelectedItem().toString());
            String extName = JOptionPane.showInputDialog("Please give this processor a special name:");
            if (extName != null) {
                if (extName.length() == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "name empty!");
                    return;
                }
            } else return;
            if (parent.RegIPProcessor(extName, new SM3IntruderPayloadProcessor(parent, extName, config)))
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


        panel4.add(label5);
        panel4.add(outFormatSelector);
        panel5.add(applyBtn);
        panel5.add(deleteBtn);

        mainPanel.add(label1);
        mainPanel.add(panel4);
        mainPanel.add(panel5);
        return mainPanel;
    }
}
