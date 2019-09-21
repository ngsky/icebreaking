package com.ngsky.ice;

/**
 * @Description:
 * @Author: ngsky
 * @CreateDate: 2019/7/11 17:02
 */
public class JavaTest {
    public static void main(String[] args) {
        int len = 32089 * 1024;
        int MB4 = 4 * 1024 * 1024;
        double dlen = (double) len;
        double dMB4 = (double) MB4;
        System.out.println("len:" + dlen);
        System.out.println("len:" + dMB4);
        System.out.println("chunk count: " + len / MB4);
        System.out.println("chunk count: " + (int) Math.ceil(dlen / dMB4));
    }
}
