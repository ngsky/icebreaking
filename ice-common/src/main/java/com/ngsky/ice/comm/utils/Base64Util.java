package com.ngsky.ice.comm.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
/**
 * <dl>
 * <dt>Base64Util</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/7/2019 11:29 AM</dd>
 * </dl>
 *
 * @author ngsky
 */
public class Base64Util {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String text = "fdjasklfjsdalfjdsfjkdlfjsdfjkfdjklsadjffffffffffffffjsadljflksd";
        System.out.println(encode(text));
        System.out.println(decode(encode(text)));
    }

    public static String encode(String text) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(text)) return null;
        final Base64.Encoder encoder = Base64.getEncoder();
        final byte[] textByte = text.getBytes("UTF-8");
        return encoder.encodeToString(textByte);
    }

    public static String decode(String text) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(text)) return null;
        final Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(text), "UTF-8");
    }
}
