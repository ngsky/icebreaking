package com.ngsky.ice.rest.utils;

/**
 * @Description:
 * @Author: ngsky
 * @CreateDate: 2019/7/11 17:16
 */
public class SizeUtil {
    // 向上取整
    public static int coundChunked(int fileSize, int chunkSize) {
        return (int) Math.ceil(((double) fileSize) / (double) chunkSize);
    }
}
