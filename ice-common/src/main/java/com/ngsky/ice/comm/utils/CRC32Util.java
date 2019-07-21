package com.ngsky.ice.comm.utils;

import java.util.zip.CRC32;

/**
 * <dl>
 * <dt>CRC32Util</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 4/5/2019 2:40 PM</dd>
 * </dl>
 *
 * @author daxiong
 */
public class CRC32Util {
    private final static String CRC32_STR = "CRC32_STR";

    public static String getCRC32(String str) {
        CRC32 crc = new CRC32();
        str = str + CRC32_STR;
        crc.update(str.getBytes());
        return Long.toHexString(crc.getValue()).toUpperCase();
    }
}
