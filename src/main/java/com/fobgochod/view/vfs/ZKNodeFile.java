package com.fobgochod.view.vfs;

import com.fobgochod.ZKClient;
import com.fobgochod.constant.ZKCli;
import com.fobgochod.constant.ZKConstant;
import com.fobgochod.util.ByteUtil;
import com.fobgochod.util.StringUtil;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.util.LocalTimeCounter;
import org.apache.zookeeper.data.Stat;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ZooKeeper node virtual file
 *
 * @author fobgochod
 * @since 1.0
 */
public class ZKNodeFile extends VirtualFile {

    protected final ZKClient zkClient = ZKClient.getInstance();

    private final long myTimeStamp = System.currentTimeMillis();
    private long myModStamp = LocalTimeCounter.currentTime();

    private final ZKNodeFileSystem myFS;
    private final String myPath;
    private final String myParentPath;
    private final String myName;

    private byte[] myContent;
    private final Stat stat;
    private boolean isLeaf;

    public ZKNodeFile(ZKNodeFileSystem FS, String path) {
        this.myFS = FS;
        this.myPath = path;
        this.myParentPath = StringUtil.pathParent(myPath);
        this.myName = StringUtil.pathName(path);
        this.stat = zkClient.exists(path);
        this.isLeaf = stat.getNumChildren() == 0;
    }

    public @NotNull String getName() {
        return myName;
    }

    public @NotNull VirtualFileSystem getFileSystem() {
        return myFS;
    }

    public @NotNull String getPath() {
        return myPath;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return !isLeaf;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public VirtualFile getParent() {
        return myParentPath == null ? null : new ZKNodeFile(this.myFS, myParentPath);
    }

    @Override
    public VirtualFile[] getChildren() {
        try {
            List<String> children = zkClient.getChildren(myPath);
            return children.stream()
                    .map(childName -> new ZKNodeFile(myFS, StringUtil.join(myPath, childName)))
                    .toArray(VirtualFile[]::new);
        } catch (Exception ignore) {
        }
        return new VirtualFile[0];
    }

    @NotNull
    public OutputStream getOutputStream(final Object requestor, final long newModificationStamp, long newTimeStamp) throws IOException {
        return new ByteArrayOutputStream() {
            @Override
            public void close() {
                myModStamp = newModificationStamp;
                myContent = toByteArray();
                zkClient.setData(myPath, myContent);
            }
        };
    }

    @Override
    public byte @NotNull [] contentsToByteArray() throws IOException {
        fillContent();
        return this.myContent;
    }

    @Override
    public long getTimeStamp() {
        return myModStamp;
    }

    @Override
    public long getModificationStamp() {
        return myTimeStamp;
    }

    @Override
    public long getLength() {
        return stat.getDataLength();
    }

    @Override
    public void refresh(boolean asynchronous, boolean recursive, Runnable postRunnable) {
    }

    @NotNull
    public InputStream getInputStream() throws IOException {
        fillContent();
        return new ByteArrayInputStream(myContent);
    }

    @NotNull
    public FileType getFileType() {
        return StringUtil.fileType(myName);
    }

    public boolean equals(Object obj) {
        return obj instanceof ZKNodeFile && ((ZKNodeFile) obj).myPath.equals(myPath);
    }

    public void setLeaf() {
        this.isLeaf = true;
    }

    public boolean isZip() {
        return myName.endsWith(ZKConstant.ZIP);
    }

    public void fillContent() {
        if (this.myContent == null) {
            byte[] data = zkClient.getData(myPath);
            if (isZip()) {
                data = ByteUtil.unzip(data);
            }
            this.myContent = ZKCli.getString(data).getBytes(getCharset());
        }
    }
}
