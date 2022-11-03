package com.fobgochod.view.window.listener;

import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.IconUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.FileTypes;
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
                panel.setIcon(IconUtil.Root);
            } else if (node.isLeaf()) {
                if (node.isEphemeral()) {
                    panel.setIcon(AllIcons.Nodes.Related);
                } else {
                    panel.setIcon(AllIcons.Nodes.Editorconfig);
                }
                if (node.isBinary()) {
                    panel.setIcon(FileTypes.ARCHIVE.getIcon());
                }
            } else {
                panel.setIcon(AllIcons.Nodes.ConfigFolder);
            }
            panel.setToolTipText(node.getTooltip());
        }
        return component;
    }
}
