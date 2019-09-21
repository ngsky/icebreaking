package com.ngsky.ice.comm.utils;

/**
 * @Description: 你不可能看懂这些
 * @Author: ngsky
 * @CreateDate: 2019/7/11 9:59
 */
public class BinaryUtil {

    /**
     * 前 5byte用于压缩,后3byte预留
     */
    private final static int MAX_SIZE = 5;

    /**
     * 二进制数字压缩算法：int -> byte[5]
     * Integer.MAX_VALUE = 2147483647, 压缩为字节流：长度最大为5
     */
    public static byte[] encodeInt(int value) {
        byte[] buf = new byte[MAX_SIZE];
        int posi = 0;
        while (true) {
            if ((value & ~0x7F) == 0) {
                buf[posi] = (byte) value;
                return buf;
            } else {
                buf[posi++] = (byte) ((value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    /**
     * 二进制解压缩算法：byte[5] -> int
     */
    public static int decodeByte(byte[] buf) {
        int posi = 0;
        int value = 0;
        for (int shift = 0; shift < 32; shift += 7) {
            final byte b = buf[posi++];
            value |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                return value;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int value = Integer.MIN_VALUE;
        System.out.println("value:" + value);
        byte[] buf = encodeInt(value);
        for (int i = 0; i < buf.length; i++) {
            System.out.println("buf:" + buf[i]);
        }
        int ret = decodeByte(buf);
        System.out.println("ret:" + ret);
    }
}
