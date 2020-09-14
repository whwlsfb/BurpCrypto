package burp.execjs;

import burp.BurpExtender;
import burp.utils.UIUtil;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class JsUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JTextField methodText;
    private JTextArea jsCodeText;
    private JButton applyBtn, deleteBtn;
    private HashMap<String, String> includes = new HashMap<>();

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

        boolean canUseCodeEditor = canUseCodeEditor();
        final JLabel label3 = new JLabel("Js Code: ");
        JScrollPane codePane = null;
        if (canUseCodeEditor) {
            jsCodeText = new RSyntaxTextArea(5, 10);
            RSyntaxTextArea textArea = (RSyntaxTextArea) jsCodeText;
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            LanguageSupportFactory.get().register(textArea);
            textArea.setMarkOccurrences(true);
            textArea.setCodeFoldingEnabled(true);
            textArea.setTabsEmulated(true);
            ToolTipManager.sharedInstance().registerComponent(textArea);
            codePane = new RTextScrollPane(jsCodeText, true);
            RTextScrollPane scrollPane = (RTextScrollPane) codePane;
            scrollPane.setIconRowHeaderEnabled(true);
            scrollPane.getGutter().setBookmarkingEnabled(true);
        } else {
            jsCodeText = new JTextArea(5, 10);
            codePane = new JScrollPane(jsCodeText);
        }
        jsCodeText.setLineWrap(true);
        JPopupMenu popupMenu = new JPopupMenu();
        JMenu menu = new JMenu("Include Snippet");
        for (Map.Entry<String, String> snippet : JsSnippets.Snippets.entrySet()) {
            JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(snippet.getKey());
            menuItem.addActionListener(e -> {
                if (menuItem.getState()) {
                    includes.put(snippet.getKey(), snippet.getValue());
                    jsCodeText.append(JsSnippets.SnippetHelps.get(snippet.getKey()) + "\r\n\r\n");
                } else {
                    includes.remove(snippet.getKey());
                    jsCodeText.setText(jsCodeText.getText().replace(JsSnippets.SnippetHelps.get(snippet.getKey()), ""));
                }
            });
            menu.add(menuItem);
        }
        JMenuItem append_simple_function = new JMenuItem("Append Simple Function");
        append_simple_function.addActionListener(e -> {
            jsCodeText.append(JsSnippets.EmptyFunction);
            methodText.setText("calc");
        });
        popupMenu.add(menu);
        popupMenu.add(append_simple_function);
        jsCodeText.setComponentPopupMenu(popupMenu);

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            JsConfig config = new JsConfig();
            config.CryptoJsCode = "";
            for (Map.Entry<String, String> snippet : includes.entrySet()) {
                config.CryptoJsCode += snippet.getValue() + "\r\n";
            }
            config.CryptoJsCode += jsCodeText.getText();
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
            if (extName != null) {
                if (extName.length() == 0) {
                    JOptionPane.showMessageDialog(mainPanel, "name empty!");
                    return;
                }
            } else return;
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

    private boolean canUseCodeEditor() {
        try {
            String[] version = parent.callbacks.getBurpVersion();
            return (Integer.parseInt(version[1]) >= 2020 && Integer.parseInt(version[2]) >= 4);  // RSyntaxTextArea code editor only support in BurpSuite 2020.4 or higher.
        } catch (Exception ex) {
            return false;
        }
    }
}
