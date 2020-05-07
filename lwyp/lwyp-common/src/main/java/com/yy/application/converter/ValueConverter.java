package com.yy.application.converter;
/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
public interface ValueConverter<V> {
    V convert(Object value, Class<?> type, Object... extraParams) throws Exception;
}
