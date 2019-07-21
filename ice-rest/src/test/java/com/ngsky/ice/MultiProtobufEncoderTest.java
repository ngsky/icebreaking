package com.ngsky.ice;

import com.ngsky.ice.comm.utils.BinaryUtil;

/**
 * @Description:
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/7/11 10:46
 */
public class MultiProtobufEncoderTest {
    public static void main(String[] args) {
        int bodyLen = 4 * 1024 * 1024;
        byte[] header = testEnodeHeader(bodyLen);
    }

    private static byte[] testEnodeHeader(int bodyLen){
        byte messageType = 0x0f;

        byte[] bodyLenHeader = BinaryUtil.encodeInt(bodyLen);
        byte[] header = new byte[8];
        header[5] = 1;
        header[6] = 1;
        header[7] = messageType;
        for (int i = 0; i < bodyLenHeader.length; i++) {
            System.out.println("bodyLenHeader----------:" + bodyLenHeader[i]);
        }
        System.arraycopy(bodyLenHeader, 0, header, 0, 5);

        for (int i = 0; i < header.length; i++) {
            System.out.println("header:::::;:" + header[i]);
        }
        return header;
    }

    private void testDecodeHeader(byte[] header) {

    }
}
