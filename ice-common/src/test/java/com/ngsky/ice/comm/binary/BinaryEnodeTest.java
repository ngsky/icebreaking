package com.ngsky.ice.comm.binary;

/**
 * @Description:
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/7/11 9:45
 */
public class BinaryEnodeTest {

    public static void main(String[] args) {
//        int len = 43489243;
//        int a = transform32ForZigzag(len);
//        System.out.println("a:" + a);
//        int b = detransform32ForZigzag(a);
//        System.out.println("b:" + b);

        System.out.println("4MB:" + 4 * 1024 * 1024);

        int value = Integer.MAX_VALUE;
//        int value = 1999999999;
        System.out.println("value:" + value);

        byte[] buffer = new byte[5];

        int position = 0;
        write32ForVarint(value, buffer, position);
        for (int i = 0; i < buffer.length; i++) {
            System.out.println("buffer:" + buffer[i]);
        }
        System.out.println(read32ForVarint(buffer, 0));
    }



    static void write32ForVarint(int value, byte[] buffer, int position) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                buffer[position++] = (byte) value;
                return;
            } else {
                buffer[position++] = (byte) ((value & 0x7F) | 0x80);//|0x80代表首位bit置1
                value >>>= 7;
            }
        }
    }

    static int read32ForVarint(byte[] buffer, int position) {
        int result = 0;
        for (int shift = 0; shift < 32; shift += 7) {//这个32 可以写29反正只要大于或等于28就行。
            final byte b = buffer[position++];
            result |= (long) (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                return result;
            }
        }
        return -1;
    }

    static int transform32ForZigzag(int num) {
        return (num << 1) ^ (num >> 31);
    }

    static int detransform32ForZigzag(int num) {
        return (num >>> 1) ^ -(num & 1);
    }

}
