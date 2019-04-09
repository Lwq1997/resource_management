package com.lwq.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Lwq
 * @Date: 2019/3/15 17:33
 * @Version 1.0
 * @Describe
 */
public class StringUtils {

    public static List<Integer> splitToListInt(String str){
        List<String> stringList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);

        return stringList.stream()
                .map(strItem -> Integer.parseInt(strItem))
                .collect(Collectors.toList());
    }
}
