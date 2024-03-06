package com.fobgochod.view.window;

import com.fobgochod.constant.StatStructure;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.constant.ZKTab;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.domain.ZKTreeModel;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.SingleUtil;
import com.fobgochod.view.editor.ZKFileTypePanel;
import com.fobgochod.view.editor.ZKNodeEditor;
import com.fobgochod.view.vfs.ZKNodeFile;
import com.fobgochod.view.vfs.ZKNodeFileSystem;
import com.fobgochod.view.window.listener.ZKTreeCellRenderer;
import com.fobgochod.view.window.listener.ZKTreeMouseListener;
import com.fobgochod.view.window.listener.ZKTreeSelectionListener;
import com.intellij.ide.ui.customization.CustomizationUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ZKToolWindow extends SimpleToolWindowPanel {

    private final transient Project project;

    private JBSplitter root;
    private JScrollPane treePane;
    private Tree tree;

    /**
     * 选项卡面板 - 节点信息
     */
    private JBTabs tabs;

    /**
     * node data
     */
    private transient TabInfo dataTab;
    private ZKNodeEditor nodeData;
    private ZKFileTypePanel zkFileTypePanel;

    /**
     * node stat
     */
    private JTable statTable;

    /**
     * node ACL
     */
    private JTable aclTable;

    /**
     * system log
     */
    private JTextPane console;

    public ZKToolWindow(@NotNull Project project) {
        super(true);
        this.project = project;

        initComponent();
        setToolbar(initToolbar().getComponent());
        setContent(root);

        initEvent();
    }

    public static ZKToolWindow getInstance() {
        return getInstance(SingleUtil.getProject());
    }

    public static ZKToolWindow getInstance(@NotNull Project project) {
        return project.getService(ZKToolWindow.class);
    }

    public Project getProject() {
        return project;
    }

    public void initTree() {
        if (tree == null) {
            tree = new Tree();
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            tree.addTreeSelectionListener(new ZKTreeSelectionListener(this));
            tree.addMouseListener(new ZKTreeMouseListener());
            tree.setCellRenderer(new ZKTreeCellRenderer());

            ToolTipManager.sharedInstance().registerComponent(tree);
            CustomizationUtil.installPopupHandler(tree, ZKConstant.ZOOKEEPER_NODE_POPUP, ActionPlaces.POPUP);
        }

        tree.setModel(new ZKTreeModel());
        treePane.setViewportView(tree);
        treePane.updateUI();
    }

    public void updateTree() {
        tree.updateUI();
        tree.expandPath(tree.getSelectionPath());
    }

    public void closeTree() {
        treePane.setViewport(null);
        treePane.updateUI();
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

    public ZKNode getSelectionNode() {
        if (tree == null) {
            NoticeUtil.error("please click Refresh to init zookeeper.");
            return null;
        }
        TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            NoticeUtil.error("please select at least one node.");
            return null;
        }
        return (ZKNode) treePath.getLastPathComponent();
    }

    public void selectionNodeChanged() {
        if (tree != null && tree.getSelectionPath() != null) {
            this.zkFileTypePanel.setSelectionNode(this.getSelectionNode());
            this.flushTabData();
        }
    }

    public String getData() {
        return nodeData.getText();
    }

    public void setData(String data) {
        nodeData.setText(data);
    }

    public void setStat(TableModel statModel) {
        statTable.setModel(statModel);
        statTable.updateUI();
    }

    public void setAcl(TableModel statModel) {
        aclTable.setModel(statModel);
        if (statModel.getColumnCount() == 3) {
            aclTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            aclTable.getColumnModel().getColumn(0).setPreferredWidth(80);
            aclTable.getColumnModel().getColumn(1).setPreferredWidth(220);
            aclTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        }
        aclTable.updateUI();
    }

    public JTextPane getConsole() {
        return console;
    }

    public ZKTab getTab() {
        TabInfo selectedInfo = tabs.getSelectedInfo();
        if (selectedInfo != null) {
            return ZKTab.valueOf(selectedInfo.getText());
        }
        return null;
    }

    public void showTab(int index) {
        TabInfo tabInfo = tabs.getTabAt(index);
        tabs.select(tabInfo, true);
    }

    public void openFile(boolean force) {
        ZKNode selectionNode = getSelectionNode();
        if (selectionNode == null) {
            return;
        }
        if (force || (selectionNode.isLeaf() && !selectionNode.isBinary())) {
            ZKNodeFile virtualFile = (ZKNodeFile) ZKNodeFileSystem.getInstance().findFileByPath(selectionNode.getFullPath());
            if (virtualFile != null) {
                if (force) {
                    virtualFile.setLeaf();
                }
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
            }
        }
    }

    private void initComponent() {
        root = new JBSplitter(true, ZKToolWindow.class.getName(), 0.5F);

        treePane = new JBScrollPane();
        root.setFirstComponent(treePane);

        tabs = new JBTabsImpl(project);

        // node data
        nodeData = new ZKNodeEditor(project);
        CustomizationUtil.installPopupHandler(nodeData, ZKConstant.ZOOKEEPER_NODE_DATA_POPUP, ActionPlaces.POPUP);
        dataTab = new TabInfo(nodeData);
        dataTab.setText(ZKTab.Data.key());
        dataTab.setIcon(ZKTab.Data.icon());
        dataTab.setTooltipText(ZKTab.Data.intro());
        tabs.addTab(dataTab);

        // node stat
        JScrollPane statTabPane = new JBScrollPane();
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
        TabInfo statTab = new TabInfo(statTabPane);
        statTab.setText(ZKTab.Stat.key());
        statTab.setIcon(ZKTab.Stat.icon());
        statTab.setTooltipText(ZKTab.Stat.intro());
        tabs.addTab(statTab);

        // node ACL
        JScrollPane aclTabPane = new JBScrollPane();
        aclTable = new JBTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        aclTabPane.setViewportView(aclTable);
        TabInfo aclTab = new TabInfo(aclTabPane);
        aclTab.setText(ZKTab.ACL.key());
        aclTab.setIcon(ZKTab.ACL.icon());
        aclTab.setTooltipText(ZKTab.ACL.intro());
        tabs.addTab(aclTab);

        // system log
        JScrollPane logTabPane = new JBScrollPane();
        console = new JTextPane();
        CustomizationUtil.installPopupHandler(console, ZKConstant.ZOOKEEPER_LOG_POPUP, ActionPlaces.POPUP);
        logTabPane.setViewportView(console);
        TabInfo logTab = new TabInfo(logTabPane);
        logTab.setText(ZKTab.Log.key());
        logTab.setIcon(ZKTab.Log.icon());
        logTab.setTooltipText(ZKTab.Log.intro());
        tabs.addTab(logTab);

        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.add(tabs.getComponent(), BorderLayout.CENTER);

        // node data file type
        zkFileTypePanel = new ZKFileTypePanel(nodeData);
        tabPanel.add(zkFileTypePanel, BorderLayout.SOUTH);

        TabInfo selectedTab = tabs.getSelectedInfo();
        if (selectedTab == null) {
            zkFileTypePanel.setVisible(false);
        } else {
            zkFileTypePanel.setVisible(dataTab.getText().equals(selectedTab.getText()));
        }

        root.setSecondComponent(tabPanel);
    }

    private ActionToolbar initToolbar() {
        ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction(ZKConstant.ZOOKEEPER_NAVIGATOR_TOOLBAR);
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true);
        toolbar.setTargetComponent(this);
        return toolbar;
    }

    private void initEvent() {
        tabs.addListener(new TabsListener() {
            @Override
            public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
                flushTabData();
            }

            @Override
            public void beforeSelectionChanged(TabInfo oldSelection, TabInfo newSelection) {
                zkFileTypePanel.setVisible(dataTab.getText().equalsIgnoreCase(newSelection.getText()));
            }
        });

        aclTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && event.getButton() == MouseEvent.BUTTON1) {
                    int selectedRow = aclTable.getSelectedRow();
                    NoticeUtil.clipboard(String.valueOf(aclTable.getValueAt(selectedRow, 1)));
                }
            }
        });
    }


    private void flushTabData() {
        TabInfo selectedInfo = tabs.getSelectedInfo();
        if (selectedInfo == null) {
            this.showTab(ZKTab.Data.ordinal());
        }

        ZKNode selectionNode = this.getSelectionNode();
        if (selectionNode == null) {
            setData(ZKConstant.EMPTY);
            setStat(new DefaultTableModel());
            setAcl(new DefaultTableModel());
            return;
        }

        String tabName = tabs.getSelectedInfo().getText();
        if (ZKTab.Data.key().equals(tabName)) {
            ZKNodeData.getInstance(project).showTabData(selectionNode);
        } else if (ZKTab.Stat.key().equals(tabName)) {
            ZKNodeData.getInstance(project).showTabStat(selectionNode);
        } else if (ZKTab.ACL.key().equals(tabName)) {
            ZKNodeData.getInstance(project).showTabAcl(selectionNode);
        }
    }
}
