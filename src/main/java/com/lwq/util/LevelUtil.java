package com.lwq.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Lwq
 * @Date: 2019/3/11 22:08
 * @Version 1.0
 * @Describe
 */
public class LevelUtil {

    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    // 0
    // 0.1
    // 0.1.2
    // 0.1.3
    // 0.4
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
