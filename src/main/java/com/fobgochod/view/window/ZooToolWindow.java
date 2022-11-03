package com.fobgochod.view.window;

import com.fobgochod.constant.StatStructure;
import com.fobgochod.constant.ZKPluginId;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.view.window.listener.ZKTreeCellRenderer;
import com.fobgochod.view.window.listener.ZKTreeMouseListener;
import com.fobgochod.view.window.listener.ZKTreeSelectionListener;
import com.intellij.icons.AllIcons;
import com.intellij.ide.ui.customization.CustomizationUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ZooToolWindow extends SimpleToolWindowPanel {

    private JBSplitter root;
    private JScrollPane treePane;
    private Tree tree;
    private JTabbedPane treeTabs;
    private JScrollPane dataTabPane;
    private JScrollPane statTabPane;
    private JScrollPane aclTabPane;
    private JScrollPane logTabPane;
    private JTextArea dataTextArea;
    private JTable statTable;
    private JTable aclTable;
    private JTextPane console;

    public ZooToolWindow() {
        super(true);

        initComponent();
        setToolbar(initToolbar().getComponent());
        setContent(root);

        initEvent();
    }

    public static ZooToolWindow getInstance() {
        return ApplicationManager.getApplication().getService(ZooToolWindow.class);
    }

    private void initComponent() {
        root = new JBSplitter(true, ZooToolWindow.class.getName(), 0.5F);

        tree = new Tree();
        treePane = new JBScrollPane();
        root.setFirstComponent(treePane);

        dataTabPane = new JBScrollPane();
        dataTextArea = new JTextArea();
        CustomizationUtil.installPopupHandler(dataTextArea, ZKPluginId.UPDATE_NODE_DATA, ActionPlaces.POPUP);
        dataTabPane.setViewportView(dataTextArea);

        statTabPane = new JBScrollPane();
        statTable = new JBTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                statTable.setToolTipText(StatStructure.values()[row].intro());
                return super.getCellRenderer(row, column);
            }
        };

        statTabPane.setViewportView(statTable);


        aclTabPane = new JBScrollPane();
        aclTable = new JBTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        aclTabPane.setViewportView(aclTable);

        logTabPane = new JBScrollPane();
        console = new JTextPane();
        CustomizationUtil.installPopupHandler(console, ZKPluginId.CLEAR_ALL_LOG, ActionPlaces.POPUP);
        logTabPane.setViewportView(console);

        treeTabs = new JBTabbedPane();

        treeTabs.addTab("Data", AllIcons.FileTypes.Text, dataTabPane, "ZNode Data");
        treeTabs.addTab("Stat", AllIcons.Ide.ConfigFile, statTabPane, "Stat Structure");
        treeTabs.addTab("ACL", AllIcons.Actions.Show, aclTabPane, "Access Control List");
        treeTabs.addTab("Log", AllIcons.Debugger.Console, logTabPane, "System journal");
        root.setSecondComponent(treeTabs);
    }

    public void initTree(TreeModel mode) {
        tree.setModel(mode);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treePane.setViewportView(tree);

        ToolTipManager.sharedInstance().registerComponent(tree);
        CustomizationUtil.installPopupHandler(tree, ZKPluginId.ZOOKEEPER_POPUP, ActionPlaces.POPUP);

        tree.updateUI();
    }

    public void flushTree() {
        tree.updateUI();
        tree.expandPath(tree.getSelectionPath());
    }

    public void expandTree() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    public void collapseTree() {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
    }

    private ActionToolbar initToolbar() {
        ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction(ZKPluginId.ZOOKEEPER_TOOLBAR);
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true);
        toolbar.setTargetComponent(this);
        return toolbar;
    }

    private void initEvent() {
        tree.addTreeSelectionListener(new ZKTreeSelectionListener(this));
        tree.addMouseListener(new ZKTreeMouseListener());
        tree.setCellRenderer(new ZKTreeCellRenderer());
    }

    public Tree getTree() {
        return tree;
    }

    public ZKNode getSelectionNode() {
        TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            NoticeUtil.error("please select at least one node.");
            return null;
        }
        return (ZKNode) treePath.getLastPathComponent();
    }

    public JTextArea getText() {
        return dataTextArea;
    }

    public JTable getStat() {
        return statTable;
    }

    public JTable getAcl() {
        return aclTable;
    }

    public JTextPane getConsole() {
        return console;
    }
}
