package com.raven.swing;

import com.raven.model.StatusType;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

public class TableStatus extends JLabel {

    public StatusType getType() {
        return type;
    }

    public TableStatus() {
        setForeground(Color.WHITE);
    }

    private StatusType type;

    public void setType(StatusType type) {
        this.type = type;
        if(type==StatusType.APPROVED)
        setText("ENTREGADA");
        if(type==StatusType.PENDING)
        setText("P/ENTREGAR");
         if(type==StatusType.REJECT)
        setText("CANCELADA");
          if(type==StatusType.ACTIVE)
        setText("ACTIVO");
           if(type==StatusType.INACTIVE)
        setText("INACTIVO");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        if (type != null) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint g;
            if (type == StatusType.PENDING) {
                g = new GradientPaint(0, 0, new Color(199, 202, 56 ), 0, getHeight(), new Color(189, 191, 51 ));
            } else if (type == StatusType.APPROVED || type == StatusType.ACTIVE) {
                g = new GradientPaint(0, 0, new Color(30, 192, 35), 0, getHeight(), new Color(25, 167, 30));
            } else {
                g = new GradientPaint(0, 0, new Color(213, 64, 12), 0, getHeight(), new Color(191, 59, 13));
            }
            g2.setPaint(g);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 1, 1);
        }
        super.paintComponent(grphcs);
    }
}
