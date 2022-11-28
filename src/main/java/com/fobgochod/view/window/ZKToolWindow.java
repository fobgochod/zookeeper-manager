package com.fobgochod.view.window;

import com.fobgochod.constant.StatStructure;
import com.fobgochod.constant.ZKPluginId;
import com.fobgochod.constant.ZKTab;
import com.fobgochod.domain.ZKNode;
import com.fobgochod.domain.ZKTreeModel;
import com.fobgochod.util.NoticeUtil;
import com.fobgochod.util.SingleUtil;
import com.fobgochod.util.ZKBundle;
import com.fobgochod.view.editor.ZKNodeEditor;
import com.fobgochod.view.vfs.ZKNodeFile;
import com.fobgochod.view.vfs.ZKNodeFileSystem;
import com.fobgochod.view.window.listener.ZKTreeCellRenderer;
import com.fobgochod.view.window.listener.ZKTreeMouseListener;
import com.fobgochod.view.window.listener.ZKTreeSelectionListener;
import com.intellij.icons.AllIcons;
import com.intellij.ide.ui.customization.CustomizationUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.FileTypeRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ZKToolWindow extends SimpleToolWindowPanel {

    private final transient Map<ZKNode, FileType> dataFileTypeCache;
    private final transient Project project;

    private JBSplitter root;
    private JScrollPane treePane;
    private Tree tree;
    /**
     * 选中的ZKNode
     */
    private transient ZKNode selectionNode;

    /**
     * 选项卡面板 - 节点信息
     */
    private JBTabs tabs;

    /**
     * node data
     */
    private transient TabInfo dataTab;
    private ZKNodeEditor nodeData;
    private ComboBox<FileType> nodeDataFileType;
    private JPanel nodeDataFileTypePanel;

    /**
     * node stat
     */
    private transient TabInfo statTab;
    private JScrollPane statTabPane;
    private JTable statTable;

    /**
     * node ACL
     */
    private transient TabInfo aclTab;
    private JScrollPane aclTabPane;
    private JTable aclTable;

    /**
     * system log
     */
    private transient TabInfo logTab;
    private JScrollPane logTabPane;
    private JTextPane console;

    public ZKToolWindow(@NotNull Project project) {
        super(true);
        this.project = project;
        this.dataFileTypeCache = new HashMap<>();

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

    @NotNull
    public FileType getCacheType() {
        if (selectionNode == null) {
            return ZKNodeEditor.TEXT_FILE_TYPE;
        }
        return dataFileTypeCache.getOrDefault(selectionNode, ZKNodeEditor.TEXT_FILE_TYPE);
    }

    public void setCacheType(@NotNull FileType fileType) {
        if (selectionNode == null) {
            return;
        }
        dataFileTypeCache.put(selectionNode, fileType);
    }

    public ZKNode getSelectionNode() {
        TreePath treePath = tree.getSelectionPath();
        if (treePath == null) {
            NoticeUtil.error("please select at least one node.");
            return null;
        }
        return (ZKNode) treePath.getLastPathComponent();
    }

    public void selectionNodeChanged(@Nullable ZKNode selectionNode) {
        this.selectionNode = selectionNode;
        this.nodeDataFileType.setSelectedItem(getCacheType());
        ZKNodeData.getInstance(project).showZNode(selectionNode);
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
        aclTable.updateUI();
    }

    public JTextPane getConsole() {
        return console;
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

        tree = new Tree();
        treePane = new JBScrollPane();
        root.setFirstComponent(treePane);

        tabs = new JBTabsImpl(project);

        // node data
        nodeData = new ZKNodeEditor(project);
        CustomizationUtil.installPopupHandler(nodeData, ZKPluginId.UPDATE_NODE_DATA, ActionPlaces.POPUP);
        dataTab = new TabInfo(nodeData);
        dataTab.setText(ZKTab.Data.key());
        dataTab.setIcon(AllIcons.FileTypes.Text);
        dataTab.setTooltipText(ZKTab.Data.intro());
        tabs.addTab(dataTab);

        // node stat
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
        statTab = new TabInfo(statTabPane);
        statTab.setText(ZKTab.Stat.key());
        statTab.setIcon(AllIcons.Ide.ConfigFile);
        statTab.setTooltipText(ZKTab.Stat.intro());
        tabs.addTab(statTab);

        // node ACL
        aclTabPane = new JBScrollPane();
        aclTable = new JBTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        aclTabPane.setViewportView(aclTable);
        aclTab = new TabInfo(aclTabPane);
        aclTab.setText(ZKTab.ACL.key());
        aclTab.setTooltipText(ZKTab.ACL.intro());
        aclTab.setIcon(AllIcons.Actions.Show);
        tabs.addTab(aclTab);

        // system log
        logTabPane = new JBScrollPane();
        console = new JTextPane();
        CustomizationUtil.installPopupHandler(console, ZKPluginId.CLEAR_ALL_LOG, ActionPlaces.POPUP);
        logTabPane.setViewportView(console);
        logTab = new TabInfo(logTabPane);
        logTab.setText(ZKTab.Log.key());
        logTab.setTooltipText(ZKTab.Log.intro());
        logTab.setIcon(AllIcons.Debugger.Console);
        tabs.addTab(logTab);

        // node data file type
        nodeDataFileTypePanel = new JPanel(new BorderLayout());
        nodeDataFileTypePanel.add(new JBLabel(ZKBundle.message("zookeeper.tab.data.FileType")), BorderLayout.WEST);
        nodeDataFileType = new ComboBox<>(new FileType[]{
                ZKNodeEditor.TEXT_FILE_TYPE,
                ZKNodeEditor.JSON_FILE_TYPE,
                ZKNodeEditor.HTML_FILE_TYPE,
                ZKNodeEditor.XML_FILE_TYPE
        });
        nodeDataFileType.setFocusable(false);
        nodeDataFileTypePanel.add(nodeDataFileType, BorderLayout.CENTER);
        nodeDataFileTypePanel.setBorder(JBUI.Borders.emptyLeft(3));

        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.add(tabs.getComponent(), BorderLayout.CENTER);
        tabPanel.add(nodeDataFileTypePanel, BorderLayout.SOUTH);

        TabInfo selectedTab = tabs.getSelectedInfo();
        if (selectedTab == null) {
            nodeDataFileTypePanel.setVisible(false);
        } else {
            nodeDataFileTypePanel.setVisible(dataTab.getText().equals(selectedTab.getText()));
        }

        root.setSecondComponent(tabPanel);
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

        tabs.addListener(new TabsListener() {
            @Override
            public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
                String selectedTab = newSelection.getText();
                if (ZKTab.Stat.key().equals(selectedTab) || ZKTab.ACL.key().equals(selectedTab)) {
                    ZKNode selectionNode = getSelectionNode();
                    if (selectionNode != null) {
                        ZKTreeModel.fillNode(selectionNode);
                        ZKNodeData.getInstance(project).showZNode(selectionNode);
                    }
                }
            }

            @Override
            public void beforeSelectionChanged(TabInfo oldSelection, TabInfo newSelection) {
                nodeDataFileTypePanel.setVisible(dataTab.getText().equalsIgnoreCase(newSelection.getText()));
            }
        });

        nodeDataFileType.setSelectedItem(getCacheType());
        nodeDataFileType.setRenderer(new FileTypeRenderer());
        nodeDataFileType.addItemListener(event -> {
            ItemSelectable selectable = event.getItemSelectable();
            if (selectable == null) {
                return;
            }
            Object[] selects = selectable.getSelectedObjects();
            if (selects == null || selects.length < 1) {
                return;
            }
            Object select = selects[0];
            if (select instanceof FileType) {
                FileType fileType = (FileType) select;
                nodeData.setFileType(fileType);
                setCacheType(fileType);
            }
        });
    }
}