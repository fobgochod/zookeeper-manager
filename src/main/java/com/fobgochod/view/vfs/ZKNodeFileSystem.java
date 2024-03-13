package com.fobgochod.view.vfs;

import com.fobgochod.ZKClient;
import com.fobgochod.util.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.ex.dummy.DummyFileSystem;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * zookeeper virtual file system
 *
 * @author fobgochod
 * @since 1.0
 */
public class ZKNodeFileSystem extends DummyFileSystem {

    private static final String PROTOCOL = "zookeeper-node";
    private final ZKClient zkClient = ZKClient.getInstance();

    public static ZKNodeFileSystem getInstance() {
        return (ZKNodeFileSystem) VirtualFileManager.getInstance().getFileSystem(PROTOCOL);
    }

    @NotNull
    @Override
    public String getProtocol() {
        return PROTOCOL;
    }

    @Override
    public ZKNodeFile findFileByPath(@NotNull String path) {
        return new ZKNodeFile(this, path);
    }

    @Override
    public void deleteFile(Object requestor, @NotNull VirtualFile vFile) throws IOException {
        zkClient.delete(vFile.getPath());
    }

    @Override
    public void moveFile(Object requestor, @NotNull VirtualFile vFile, @NotNull VirtualFile newParent) {
        byte[] content = zkClient.getData(vFile.getPath());
        zkClient.create(newParent.getPath(), content);
        zkClient.delete(vFile.getPath());
    }

    @Override
    public void renameFile(Object requestor, @NotNull VirtualFile vFile, @NotNull String newName) {
        String newFilePath = StringUtil.join(vFile.getParent().getPath(), newName);
        moveFile(requestor, vFile, new ZKNodeFile(this, newFilePath));
    }

    @NotNull
    @Override
    public VirtualFile createChildFile(Object requestor, @NotNull VirtualFile vDir, @NotNull String fileName) throws IOException {
        String filePath = StringUtil.join(vDir.getPath(), fileName);
        zkClient.create(filePath);
        return new ZKNodeFile(this, filePath);
    }

    @NotNull
    @Override
    public VirtualFile createChildDirectory(Object requestor, @NotNull VirtualFile vDir, @NotNull String dirName) {
        String filePath = StringUtil.join(vDir.getPath(), dirName);
        zkClient.create(filePath);
        return new ZKNodeFile(this, filePath);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
