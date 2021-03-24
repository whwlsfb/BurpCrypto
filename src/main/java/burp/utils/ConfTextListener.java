package burp.utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ConfTextListener implements DocumentListener {
    private IConfListener callback;
    private JTextField target;

    public ConfTextListener(JTextField _target, IConfListener _callback) {
        this.callback = _callback;
        this.target = _target;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        callback.updateConf(target.getText());
    }
}
