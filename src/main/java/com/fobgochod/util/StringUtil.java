package com.fobgochod.util;

import com.fobgochod.constant.ZKConstant;
import com.fobgochod.settings.ZKSettings;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * StringUtil.java
 *
 * @author fobgochod
 * @see com.intellij.openapi.util.text.StringUtil
 * @since 1.0
 */
public class StringUtil {

    public static boolean isEmpty(String s) {
        return s == null || ZKConstant.EMPTY.equals(s);
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * 截取字符串开头和结尾指定的字符(/)
     * /hello/world/ => hello/world
     */
    public static String trim(String str) {
        if (str.startsWith(ZKConstant.SLASH)) {
            str = str.substring(1);
        }
        if (str.endsWith(ZKConstant.SLASH)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 返回path  去掉、补上多余 /
     * / => ""
     * /hello => /hello
     * /hello/world/ = > /hello/world
     */
    public static String getPath(String str) {
        if (isEmpty(str) || ZKConstant.SLASH.equals(str)) {
            return ZKConstant.EMPTY;
        }
        return rebuild(str, ZKConstant.EMPTY);
    }

    public static String trimLeft(String str) {
        if (str.startsWith(ZKConstant.SLASH)) {
            str = str.substring(1);
        }
        return str;
    }

    public static String trimRight(String str) {
        if (str.endsWith(ZKConstant.SLASH)) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String join(String parent, String path) {
        if (parent.equals(ZKConstant.SLASH)) {
            return parent + path;
        } else {
            return parent + ZKConstant.SLASH + path;
        }
    }

    /**
     * 组合两段路由，去掉多余的 /
     *
     * @param first  /java/lang/
     * @param second /reflect/method/
     * @return /java/lang/reflect/method
     */
    public static String rebuild(String first, String second) {
        String full = first + ZKConstant.SLASH + second;
        String[] array = full.split(ZKConstant.SLASH);
        return ZKConstant.SLASH + Arrays.stream(array).filter(p -> !p.isEmpty()).collect(Collectors.joining(ZKConstant.SLASH));
    }

    public static Charset charset() {
        return charset(ZKSettings.getInstance().getCharset());
    }

    public static Charset charset(String charsetName) {
        try {
            return Charset.forName(charsetName);
        } catch (Exception ignored) {
        }
        return StandardCharsets.UTF_8;
    }
}
