package com.fobgochod.view.window.listener;

import com.fobgochod.domain.ZKNode;
import com.fobgochod.view.window.ZKToolWindow;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class ZKTreeSelectionListener implements TreeSelectionListener {

    private final ZKToolWindow toolWindow;

    public ZKTreeSelectionListener(ZKToolWindow toolWindow) {
        this.toolWindow = toolWindow;
    }

    @Override
    public void valueChanged(TreeSelectionEvent event) {
        ZKNode selectionNode = toolWindow.getSelectionNode();
        if (selectionNode != null) {
            toolWindow.selectionNodeChanged(selectionNode);
        }
    }
}
