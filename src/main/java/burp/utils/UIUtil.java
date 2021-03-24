package burp.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIUtil {
    public static JPanel GetXJPanel() {
        JPanel panel1 = new JPanel();
        panel1.setAlignmentX(0.0f);
        panel1.setLayout(new BoxLayout(panel1, 0));
        panel1.setForeground(new Color(249, 130, 11));
        panel1.setBorder(new EmptyBorder(5, 0, 5, 0));
        return panel1;
    }

    public static JPanel GetYJPanel() {
        JPanel panel1 = new JPanel();
        panel1.setAlignmentX(0.0f);
        panel1.setLayout(new BoxLayout(panel1, 1));
        panel1.setForeground(new Color(249, 130, 11));
        panel1.setBorder(new EmptyBorder(5, 0, 5, 0));
        return panel1;
    }
}
