package com.fobgochod.view.vfs;

import com.fobgochod.ZKClient;
import com.fobgochod.constant.ZKCli;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.util.LocalTimeCounter;
import org.apache.zookeeper.data.Stat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * ZooKeeper node virtual file
 *
 * @author fobgochod
 * @since 1.0
 */
public class ZKNodeFile extends VirtualFile {

    protected final ZKClient zkClient = ZKClient.getInstance();

    private final long myTimeStamp = System.currentTimeMillis();
    private final String myPath;
    private final ZKNodeFileSystem myFS;
    private byte[] myContent;

    private final String fileName;
    private boolean isLeaf;
    private final Stat stat = new Stat();
    private long myModStamp = LocalTimeCounter.currentTime();

    public ZKNodeFile(ZKNodeFileSystem FS, String path) {
        this.myPath = path;
        this.myFS = FS;
        this.fileName = path.substring(path.lastIndexOf("/") + 1);
        this.checkContent();
        this.isLeaf = stat.getNumChildren() == 0;
    }

    public static byte[] unzip(byte[] zipContent) {
        try {
            ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipContent));
            zis.getNextEntry();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int b;
            while ((b = zis.read()) != -1) {
                bos.write(b);
            }
            zis.closeEntry();
            zis.close();
            return bos.toByteArray();
        } catch (Exception ignored) {
        }
        return null;
    }

    public static byte[] zip(String name, byte[] content) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zipOutput = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry(name);
            entry.setSize(content.length);
            zipOutput.putNextEntry(entry);
            zipOutput.write(content);
            zipOutput.closeEntry();
            zipOutput.close();
            return bos.toByteArray();
        } catch (Exception ignored) {
        }
        return null;
    }

    public void setLeaf() {
        this.isLeaf = true;
    }

    @NotNull
    public String getName() {
        return this.fileName;
    }

    @NotNull
    public VirtualFileSystem getFileSystem() {
        return this.myFS;
    }

    public @NotNull String getPath() {
        String path = "/";
        if (myPath.lastIndexOf("/") > 0) {
            path = myPath.substring(0, myPath.lastIndexOf("/"));
        }
        return path;
    }

    public String getMyPath() {
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
        if ("/".equals(myPath)) {
            return null;
        }
        int slashIndex = myPath.lastIndexOf("/");
        if (slashIndex == 0) {
            return new ZKNodeFile(this.myFS, "/");
        } else {
            String parentPath = myPath.substring(0, slashIndex);
            return new ZKNodeFile(this.myFS, parentPath);
        }
    }

    @Override
    public VirtualFile[] getChildren() {
        try {
            List<String> children = zkClient.getChildren(myPath);
            if (!children.isEmpty()) {
                VirtualFile[] files = new VirtualFile[children.size()];
                for (int i = 0; i < children.size(); i++) {
                    String childName = children.get(i);
                    files[i] = new ZKNodeFile(myFS, myPath.endsWith("/") ? myPath + childName : myPath + "/" + childName);
                }
                return files;
            }
        } catch (Exception ignore) {

        }
        return new VirtualFile[0];
    }

    @NotNull
    public OutputStream getOutputStream(final Object requestor, final long newModificationStamp, long newTimeStamp) throws IOException {
        return new ByteArrayOutputStream() {
            @Override
            public void close() {
                // disable save to update node operation
                setContent(requestor, toByteArray(), newModificationStamp);
            }
        };
    }

    @Override
    public byte @NotNull [] contentsToByteArray() throws IOException {
        checkContent();
        return this.myContent;
    }

    public void checkContent() {
        if (this.myContent == null && zkClient.isConnected()) {
            byte[] data = zkClient.storingStatIn(myPath, stat);
            this.myContent = ZKCli.getString(data).getBytes();

            if (isSingleFileZip()) {
                this.myContent = unzip(myContent);
            }
        }
        if (this.myContent == null) {
            this.myContent = new byte[0];
        }
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
        checkContent();
        return this.myContent.length;
    }

    @Override
    public void refresh(boolean asynchronous, boolean recursive, Runnable postRunnable) {

    }

    @NotNull
    public InputStream getInputStream() throws IOException {
        checkContent();
        return new ByteArrayInputStream(myContent);
    }

    @NotNull
    public FileType getFileType() {
        String newFileName = this.fileName;
        if (isSingleFileZip()) {
            newFileName = newFileName.replace(".zip", "");
        }
        FileType fileType = FileTypeManager.getInstance().getFileTypeByFileName(newFileName);
        if (fileType.getName().equalsIgnoreCase(FileTypes.UNKNOWN.getName())) {
            return FileTypes.PLAIN_TEXT;
        }
        return fileType;
    }

    public boolean equals(Object obj) {
        return obj instanceof ZKNodeFile && ((ZKNodeFile) obj).myPath.equals(myPath);
    }

    @Override
    public String toString() {
        return this.myPath;
    }

    public boolean isSingleFileZip() {
        return isLeaf && fileName.endsWith(".zip") && fileName.replace(".zip", "").contains(".");
    }

    public void setContent(@Nullable Object requestor, byte[] content, long newModificationStamp) {
        myModStamp = newModificationStamp;
        this.myContent = content;
        zkClient.setData(this.myPath, content);
    }
}
