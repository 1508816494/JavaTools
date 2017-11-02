package com.xf.tpm.core.util;

/**
 * @author xufeng on 2017/11/2
 */
public class SystemUtil {
    public SystemUtil() {
    }

    public static String getEndLine() {
        return System.getProperty("line.separator");
    }

    public static int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }
}
