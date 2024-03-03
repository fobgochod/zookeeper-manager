package com.fobgochod.view.window.listener;

import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.ZKIcons;
import com.fobgochod.util.ZKPaths;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileTypes;
import org.apache.zookeeper.CreateMode;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.WrappingIconPanel;

import javax.swing.*;
import java.awt.*;

public class ZKTreeCellRenderer extends DefaultTreeRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof ZKNode && component instanceof WrappingIconPanel) {
            ZKNode node = (ZKNode) value;
            WrappingIconPanel panel = (WrappingIconPanel) component;
            if (node.isRoot()) {
                panel.setIcon(ZKIcons.ZOOKEEPER);
            } else if (node.isLeaf()) {
                CreateMode mode = ZKPaths.mode(node);
                panel.setIcon(ZKPaths.getIcon(mode));

                if (node.isBinary()) {
                    panel.setIcon(FileTypes.ARCHIVE.getIcon());
                }
            } else {
                panel.setIcon(AllIcons.Nodes.Package);
            }
            panel.setToolTipText(node.getFullPath());
        }
        return component;
    }


}
