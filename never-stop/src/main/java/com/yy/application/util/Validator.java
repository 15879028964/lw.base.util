package com.yy.application.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
public class Validator {
    private Validator() {
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static boolean isNullOrEmpty(Object value) {
        if (null == value) {
            return true;
        } else if (value instanceof CharSequence) {
            return StringUtils.isBlank(String.valueOf(value));
        } else {
            return isCollectionsSupportType(value) && CollectionUtils.isEmpty((Collection<?>) value);
        }
    }

    public static boolean isNotNullOrEmpty(Object value) {
        return !isNullOrEmpty(value);
    }

    private static boolean isCollectionsSupportType(Object value) {
        boolean isCollectionOrMap = value instanceof Collection || value instanceof Map;
        boolean isEnumerationOrIterator = value instanceof Enumeration || value instanceof Iterator;
        return isCollectionOrMap || isEnumerationOrIterator || value.getClass().isArray();
    }
}
