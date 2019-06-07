/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.loyder.software.main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import javax.swing.JComponent;

/**
 *
 * @author Luis Perez
 */
public class ComponentPrintable implements Printable {

    private final Component mComponent;

    public ComponentPrintable(Component c) {
        mComponent = c;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        boolean wasBuffered = disableDoubleBuffering(mComponent);
        mComponent.paint(g2);
        restoreDoubleBuffering(mComponent, wasBuffered);
        return PAGE_EXISTS;
    }

    private boolean disableDoubleBuffering(Component c) {
        if (c instanceof JComponent == false) {
            return false;
        }
        JComponent jc = (JComponent) c;
        boolean wasBuffered = jc.isDoubleBuffered();
        jc.setDoubleBuffered(false);
        return wasBuffered;
    }

    private void restoreDoubleBuffering(Component c, boolean wasBuffered) {
        if (c instanceof JComponent) {
            ((JComponent) c).setDoubleBuffered(wasBuffered);
        }
    }
}

