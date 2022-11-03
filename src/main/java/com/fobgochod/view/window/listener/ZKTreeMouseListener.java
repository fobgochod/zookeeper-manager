package com.fobgochod.view.window.listener;

import com.fobgochod.view.vfs.ZKNodeFileSystem;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ZKTreeMouseListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2 && event.getButton() == MouseEvent.BUTTON1) {
            Tree source = (Tree) event.getSource();
            TreePath treePath = source.getSelectionPath();
            if (treePath != null) {
                ZKNodeFileSystem.getInstance().openFile(treePath.getLastPathComponent());
            }
        }
    }
}
