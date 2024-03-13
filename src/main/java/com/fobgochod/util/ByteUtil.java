package com.fobgochod.util;

import com.fobgochod.constant.ZKConstant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ByteUtil {

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
        return ZKConstant.EMPTY_BYTE;
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
        return ZKConstant.EMPTY_BYTE;
    }
}
