package burp.aes;

import burp.BurpExtender;
import burp.UIUtil;
import burp.utils.ConfTextListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;

public class AesUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JComboBox<String> aesAlgSelecter;
    private JTextField aesKeyText;
    private JTextField aesIVText;

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

        final JLabel label2 = new JLabel("AES Alg: ");
        aesAlgSelecter = new JComboBox(GetAesAlgs());
        aesAlgSelecter.setMaximumSize(aesAlgSelecter.getPreferredSize());
        aesAlgSelecter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String item = (String) e.getItem();
                AesAlgorithms alg = AesAlgorithms.valueOf(item.replace('/', '_'));
                panel3.setVisible(!item.startsWith("AES/ECB/"));
                parent.aesConfig.Algorithms = alg;
                parent.AesUtil = new AesUtil(alg);
            }
        });
        aesAlgSelecter.setSelectedIndex(0);

        final JLabel label3 = new JLabel("AES Key: ");
        aesKeyText = new JTextField(200);
        aesKeyText.setMaximumSize(aesKeyText.getPreferredSize());
        aesKeyText.getDocument().addDocumentListener(
                new ConfTextListener(aesKeyText, value -> {
                    parent.aesConfig.Key = value;
                })
        );

        final JLabel label4 = new JLabel("AES IV: ");
        aesIVText = new JTextField(200);
        aesIVText.setMaximumSize(aesIVText.getPreferredSize());
        aesIVText.getDocument().addDocumentListener(
                new ConfTextListener(aesIVText, value -> {
                    parent.aesConfig.IV = value;
                })
        );

        panel1.add(label2);
        panel1.add(aesAlgSelecter);
        panel2.add(label3);
        panel2.add(aesKeyText);
        panel3.add(label4);
        panel3.add(aesIVText);

        mainPanel.add(label1);
        mainPanel.add(panel1);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
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

}
