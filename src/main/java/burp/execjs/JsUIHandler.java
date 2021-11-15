package burp.execjs;

import burp.BurpExtender;
import burp.utils.KeyFormat;
import burp.utils.UIUtil;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.js.JavaScriptCompletionProvider;
import org.fife.rsta.ac.js.JavaScriptLanguageSupport;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsUIHandler {
    private BurpExtender parent;
    private JPanel mainPanel;
    private JTextField methodText;
    private JPanel codePanel;
    private JTextArea jsCodeText;
    private JScrollPane codePane;
    private JComboBox<String> jsEngineSelector;
    private JButton applyBtn, deleteBtn, includeLibBtn;
    private JCheckBox useSyntaxEditor;
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
        codePanel = UIUtil.GetXJPanel();
        final JPanel panel4 = UIUtil.GetXJPanel();

        final JLabel label2 = new JLabel("Js Method Name: ");
        methodText = new JTextField(200);
        methodText.setMaximumSize(methodText.getPreferredSize());

        final JLabel label4 = new JLabel("Js Engine: ");
        jsEngineSelector = new JComboBox(GetJsEngines());
        jsEngineSelector.setMaximumSize(jsEngineSelector.getPreferredSize());
        jsEngineSelector.setSelectedIndex(0);

        final JLabel label3 = new JLabel("Js Code: ");
        useSyntaxEditor = new JCheckBox("Use Syntax highlight Editor(Experiment)");
        if (canUseCodeEditor()) {
            useSyntaxEditor.setSelected(true);
        } else {
            useSyntaxEditor.setEnabled(false);
            useSyntaxEditor.setText("Use Syntax highlight Editor(Experiment, only support in BurpSuite 2020.4 or higher.)");
        }
        useSyntaxEditor.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.initEditor(true);
            } else {
                this.initEditor(false);
            }
        });
        this.initEditor(useSyntaxEditor.isSelected());

        applyBtn = new JButton("Add processor");
        applyBtn.setMaximumSize(applyBtn.getPreferredSize());
        applyBtn.addActionListener(e -> {
            JsEngines jsEngine = JsEngines.valueOf(jsEngineSelector.getSelectedItem().toString());
            JsConfig config = new JsConfig();
            config.JsEngine = jsEngine;
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

        includeLibBtn = new JButton("Include(Test)...");
        includeLibBtn.setMaximumSize(includeLibBtn.getPreferredSize());
        includeLibBtn.addActionListener(e -> {
            JsSnippet[] snippet = JsSnippets.GetSnippetList();
            JOptionPane.showMessageDialog(mainPanel, snippet[0].name);
        });


        panel4.add(label3);
        panel4.add(useSyntaxEditor);
        panel4.add(includeLibBtn);
        panel1.add(label2);
        panel1.add(methodText);
        panel1.add(label4);
        panel1.add(jsEngineSelector);
        panel2.add(applyBtn);
        panel2.add(deleteBtn);

        mainPanel.add(label1);
        mainPanel.add(panel4);
        mainPanel.add(codePanel);
        mainPanel.add(panel1);
        mainPanel.add(panel2);

        return mainPanel;
    }

    private void initEditor(boolean useSyntax) {
        if (useSyntax) {
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
        jsCodeText.setEnabled(true);
        codePanel.removeAll();
        codePanel.add(codePane);
    }

    private boolean canUseCodeEditor() {
        try {
            String[] version = parent.callbacks.getBurpVersion();
            return (((Double.parseDouble(version[1]) > 2020) ||
                    (Double.parseDouble(version[1]) == 2020 && Double.parseDouble(version[2]) >= 4)));  // RSyntaxTextArea code editor only support in BurpSuite 2020.4 or higher.
        } catch (Exception ex) {
            return false;
        }
    }

    private String[] GetJsEngines() {
        ArrayList<String> strs = new ArrayList<String>();
        JsEngines[] items = JsEngines.values();
        for (JsEngines item : items) {
            strs.add(item.name());
        }
        return strs.toArray(new String[strs.size()]);
    }
}
