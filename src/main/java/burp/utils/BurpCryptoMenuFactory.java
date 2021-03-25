package burp.utils;

import burp.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BurpCryptoMenuFactory implements IContextMenuFactory {

    private BurpExtender parent;

    public BurpCryptoMenuFactory(BurpExtender parent) {
        this.parent = parent;
    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        ArrayList<JMenuItem> menus = new ArrayList<>();
        //if (invocation.getToolFlag() == parent.callbacks.TOOL_INTRUDER) {
        JMenuItem menu1 = new JMenuItem("Get PlainText");
        menu1.addActionListener(e -> {
            IHttpRequestResponse[] resps = invocation.getSelectedMessages();
            if (resps.length > 0) {
                IHttpRequestResponse req = resps[0];
                byte[] request = req.getRequest();
                String selectedText = getSelectedText(request, invocation.getSelectionBounds());
                if (selectedText != null && selectedText != "") {
                    String plainText = searchKey(selectedText);
                    if (plainText != null && plainText != "") {
                        ShowCopiableMessage(plainText, "This message plaintext is: ");
                        req.setComment(plainText);
                    } else {
                        JOptionPane.showMessageDialog(menu1, "Not found!");
                    }
                }
            }
        });
        menus.add(menu1);
        //}
        JMenu quickCrypto = new JMenu("Quick Crypto");
        for (IIntruderPayloadProcessor entry : parent.IPProcessors.values()) {
            JMenuItem _menu = new JMenuItem(entry.getProcessorName());
            _menu.addActionListener(e -> {
                IHttpRequestResponse req = invocation.getSelectedMessages()[0];
                byte[] request = req.getRequest();
                int[] selectedIndexRange = invocation.getSelectionBounds();
                byte[] selectedBytes = getSelectedBytes(request, selectedIndexRange);
                if (selectedBytes != null && selectedBytes.length > 0) {
                    byte[] encryptResult = entry.processPayload(selectedBytes, selectedBytes, selectedBytes);
                    if (encryptResult != null) {
                        if (invocation.getToolFlag() == parent.callbacks.TOOL_INTRUDER || invocation.getToolFlag() == parent.callbacks.TOOL_REPEATER) {
                            req.setRequest(Replace(request, selectedIndexRange, encryptResult));
                        } else {
                            ShowCopiableMessage(new String(encryptResult), "CipherText result: ");
                        }
                    } else {
                        JOptionPane.showMessageDialog(_menu, "has error!");
                    }
                }
            });
            quickCrypto.add(_menu);
        }
        if (quickCrypto.getItemCount() > 0) {
            menus.add(quickCrypto);
        }
        return menus;
    }

    public void ShowCopiableMessage(String message, String title) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JTextArea ta = new JTextArea(5, 20);
                ta.setText(message);
                ta.setWrapStyleWord(true);
                ta.setLineWrap(true);
                ta.setCaretPosition(0);
                ta.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(ta), title, JOptionPane.INFORMATION_MESSAGE);
            }
        });
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
            return new String(getSelectedBytes(request, selectedIndexRange));
        } catch (Exception ex) {
            return null;
        }
    }

    private byte[] getSelectedBytes(byte[] request, int[] selectedIndexRange) {
        try {
            byte[] selectedText = new byte[selectedIndexRange[1] - selectedIndexRange[0]];
            System.arraycopy(request, selectedIndexRange[0], selectedText, 0, selectedText.length);
            return selectedText;
        } catch (Exception ex) {
            return null;
        }
    }

    public static byte[] Replace(byte[] request, int[] selectedIndexRange, byte[] targetBytes) {
        byte[] result = new byte[request.length - (selectedIndexRange[1] - selectedIndexRange[0]) + targetBytes.length];
        System.arraycopy(request, 0, result, 0, selectedIndexRange[0]);
        System.arraycopy(targetBytes, 0, result, selectedIndexRange[0], targetBytes.length);
        System.arraycopy(request, selectedIndexRange[1], result, selectedIndexRange[0] + targetBytes.length, request.length - selectedIndexRange[1]);
        return result;
    }
}
