package com.hss.utils;

/**
 * <p>
 *
 * </p>
 *
 * @author Hss
 * @date 2024-08-24
 */
public class XmlUtil {

    /**
     * 转义
     * @param input
     * @return
     */
    public static String escapeXml(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");
    }

    /**
     * 解义
     * @param input
     * @return
     */
    public static String analyzeXml(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&apos;", "'");
    }
}
