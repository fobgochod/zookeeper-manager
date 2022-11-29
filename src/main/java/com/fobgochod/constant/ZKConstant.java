package com.fobgochod.constant;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ZKConstant {

    public static final String EMPTY = "";
    public static final String SLASH = "/";
    public static final byte[] EMPTY_BYTE = new byte[0];

    public static final DateTimeFormatter DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 弹窗内容默认大小
     */
    public static final Dimension DIALOG_SIZE = new Dimension(460, 200);
}
