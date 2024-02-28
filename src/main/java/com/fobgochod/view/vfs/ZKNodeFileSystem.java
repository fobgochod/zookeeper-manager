package com.fobgochod.view.vfs;

import com.fobgochod.ZKClient;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.ex.dummy.DummyFileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    @Override
    public VirtualFile findFileByPath(@NotNull String path) {
        return new ZKNodeFile(this, path);
    }

    @Override
    public void deleteFile(Object o, @NotNull VirtualFile virtualFile) throws IOException {
        zkClient.delete(virtualFile.getPath());
    }

    @Override
    public void moveFile(Object o, @NotNull VirtualFile virtualFile, @NotNull VirtualFile virtualFile2) {
        byte[] content = zkClient.getData(virtualFile.getPath());
        zkClient.create(virtualFile2.getPath(), content);
        zkClient.delete(virtualFile.getPath());
    }

    @Override
    public void renameFile(Object o, @NotNull VirtualFile virtualFile, @NotNull String name) {
        String newFilePath = virtualFile.getPath().substring(0, virtualFile.getPath().indexOf("/")) + "/" + name;
        moveFile(o, virtualFile, new ZKNodeFile(this, newFilePath));
    }

    @NotNull
    @Override
    public VirtualFile createChildFile(Object o, @NotNull VirtualFile virtualFile, @NotNull String fileName) throws IOException {
        String filePath = virtualFile.getPath() + "/" + fileName;
        zkClient.create(filePath);
        return new ZKNodeFile(this, filePath);
    }

    @NotNull
    @Override
    public VirtualFile createChildDirectory(Object o, @NotNull VirtualFile virtualFile, @NotNull String directory) {
        String filePath = virtualFile.getPath() + "/" + directory;
        zkClient.create(filePath);
        return new ZKNodeFile(this, filePath);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
