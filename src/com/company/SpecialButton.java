package com.company;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SpecialButton extends JButton {

    public SpecialButton(String text, int width, int height, int x, int y, ButtonListener listener) {
        super(text);
        setBorder(new EtchedBorder());
        setBackground(new Color(230, 230, 230));
        setSize(width, height);
        setLocation(x, y);
        setFocusPainted(false);
        addMouseListener(listener);
    }

    public SpecialButton(String text, int width, int height, int x, int y) {
        this(text, width, height, x, y, new ButtonListener());
    }

    public static class ButtonListener extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            ((JButton) e.getSource()).setBorder(new BevelBorder(BevelBorder.RAISED));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JButton) e.getSource()).setBorder(new EtchedBorder());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            ((JButton) e.getSource()).setEnabled(false);
            ((JButton) e.getSource()).setBorder(new BevelBorder(BevelBorder.LOWERED));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ((JButton) e.getSource()).setEnabled(true);
            ((JButton) e.getSource()).setBorder(new EtchedBorder());
        }
    }
}
