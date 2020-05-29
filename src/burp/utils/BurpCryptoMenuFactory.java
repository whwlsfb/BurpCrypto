package burp.utils;

import burp.BurpExtender;
import burp.IContextMenuFactory;
import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BurpCryptoMenuFactory implements IContextMenuFactory {

    private BurpExtender parent;

    public BurpCryptoMenuFactory(BurpExtender parent) {
        this.parent = parent;
    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        ArrayList<JMenuItem> menus = new ArrayList<>();
        if (invocation.getToolFlag() == parent.callbacks.TOOL_INTRUDER) {
            JMenuItem menu1 = new JMenuItem("Get PlainText");
            menu1.addActionListener(e -> {
                IHttpRequestResponse req = invocation.getSelectedMessages()[0];
                byte[] request = req.getRequest();
                String selectedText = getSelectedText(request, invocation.getSelectionBounds());
                if (selectedText != null && selectedText != "") {
                    String plainText = searchKey(selectedText);
                    if (plainText != null && plainText != "") {
                        JOptionPane.showMessageDialog(menu1, "This message plaintext is: \r\n" + plainText);
                        req.setComment(plainText);
                    } else {
                        JOptionPane.showMessageDialog(menu1, "Not found!");
                    }
                }
            });
            menus.add(menu1);
        }
        return menus;
    }

    private String searchKey(String key) {
        String value = parent.dict.Search(key);
        if (value == null) {
            value = parent.dict.Search(parent.helpers.urlDecode(key));
        }
        return value;
    }

    private String getSelectedText(byte[] request, int[] selectedIndexRange) {
        try {
            byte[] selectedText = new byte[selectedIndexRange[1] - selectedIndexRange[0]];
            System.arraycopy(request, selectedIndexRange[0], selectedText, 0, selectedText.length);
            return new String(selectedText);
        } catch (Exception ex) {
            return null;
        }
    }
}
