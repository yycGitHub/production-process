package com.surekam.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** 
 * <p>字符编码格式操作共同类</p> 
 * 2016年10月24日 下午9:59:38 
 */
public class EncodingUtils {

    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String ISO_8859_1 = "ISO-8859-1";

    private EncodingUtils() {
    }

    /**  
     * <p>按UTF_8格式解析字符串</p>
     * @param value 待解析的字符串
     * @return String:解析后的字符串
     */
    public static String urlEncodeUTF8(String value) {
        try {
            return URLEncoder.encode(value, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
    }

    /**  
     * <p>将ISO_8859_1格式的数据转化为UTF_8格式</p>
     * @param value 待转化的字符串
     * @return String:转化后的字符串
     */
    public static String isoToUtf(String value) {
        try {
            return new String(value.getBytes(ISO_8859_1), UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
    }

    /**  
     * <p>将UTF_8格式的数据转化为ISO_8859_1格式</p>
     * @param value 待转化的字符串
     * @return String:转化后的字符串
     */
    public static String utfToIso(String value) {
        try {
            return new String(value.getBytes(UTF_8), ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
    }

    /**  
     * <p>将UTF_8格式的数据转化为GBK格式</p>
     * @param value 待转化的字符串
     * @return String:转化后的字符串
     */
    public static String utfToGbk(String value) {
        try {
            return new String(value.getBytes(UTF_8), GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
    }
    /**  
     * <p>将ISO_8859_1格式的数据转化为GBK格式</p>
     * @param value 待转化的字符串
     * @return String:转化后的字符串
     */
    public static String isoToGbk(String value) {
        try {
            return new String(value.getBytes(ISO_8859_1), GBK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
    }
    /**  
     * <p>将GBK格式的数据转化为ISO_8859_1格式</p>
     * @param value 待转化的字符串
     * @return String:转化后的字符串
     */
    public static String gbkToIso(String value) {
        try {
            return new String(value.getBytes(GBK), ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
    }
    /**  
     * <p>将GBK格式的数据转化为UTF_8格式</p>
     * @param value 待转化的字符串
     * @return String:转化后的字符串
     */
    public static String gbkToUtf(String value) {
        try {
            return new String(value.getBytes(GBK), UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
    }
}
