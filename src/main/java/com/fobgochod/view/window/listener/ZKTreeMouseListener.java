package com.fobgochod.view.window.listener;

import com.fobgochod.view.window.ZKToolWindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ZKTreeMouseListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2 && event.getButton() == MouseEvent.BUTTON1) {
            ZKToolWindow.getInstance().openFile(false);
        }
    }
}
