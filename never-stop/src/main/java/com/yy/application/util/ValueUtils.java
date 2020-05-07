package com.yy.application.util;
//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| 1 |||// \
//                     / _||||| -9- |||||- \
//                       | | \\\ 9 /// | |
//                     | \_| ''\-1-/'' | |
//                      \ .-\__ `0` ___/-. /
//                   ___`. .' /--9--\ `. . __
//                ."" '< `.___\_<3>_/___.' >'"".
//               | | : `- \`.;`\ 0 /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖保佑                  永无BUG

import com.yy.application.converter.ValueConverter;
import com.yy.application.converter.ValueConverterRegistry;

import java.lang.reflect.Array;
import java.util.Map;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年03月20日 11:01
 */
public class ValueUtils {

    public static <T> T ifNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static String ifBlank(String value, String defaultValue) {
        return hasText(value) ? value : defaultValue;
    }

    public static <T> T ifTrue(T value, T expectValue, T elseValue) {
        if (value == expectValue || expectValue.equals(value)) {
            return value;
        }
        return elseValue;
    }

    public static <T> T ifNot(T value, T unexpectValue, T elseValue) {
        if (value != unexpectValue || !unexpectValue.equals(value)) {
            return value;
        }
        return elseValue;
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String safe2Str(Object obj) {
        return safe2Str(obj, "");
    }

    public static String safe2Str(Object obj, String nullValue) {
        if (obj != null) {
            return obj.toString();
        }
        return nullValue;
    }

    public static <T> T[] convertArray(Object[] values, Class<T> type) {
        if (values == null) {
            return null;
        }
        Object[] rslt = (Object[]) Array.newInstance(type, values.length);
        int idx = 0;
        for (Object val : values) {
            rslt[idx++] = convert(val, type);
        }
        return (T[]) rslt;
    }

    public static <T> T convert(Object value, Class<T> type) {
        return convert(value, type, null);
    }

    /**
     * <h3>给定值，返回期望的类型</h3>
     *
     * @param <T>
     * @param value
     * @param type
     * @return
     */
    public static <T> T convert(Object value, Class<T> type, Object[] extraParams) {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            if (String.class.isAssignableFrom(type)) {
                return (T) value;
            }
            if (!hasText((String) value)) {
                return null;
            }
        }
        if (type == null) {
            return (T) value;
        }
        try {
            if (type.isInstance(value)) {
                return (T) value;
            }

            Map<Class<?>, ValueConverter<?>> converterMap = ValueConverterRegistry.getInstance().getConverters();
            ValueConverter<T> converter = (ValueConverter<T>) converterMap.get(type);
            if (converter == null) {
                if (type.isArray()) {
                    converter = (ValueConverter<T>) converterMap.get(Array.class);
                } else {
                    throw new RuntimeException("The type " + type + " has not supported yet.");
                }
            }
            T v = null;
            if (extraParams != null) {
                v = (T) converter.convert(value, type, extraParams);
            } else {
                v = (T) converter.convert(value, type);
            }
            if (v != null) {
                return v;
            }
            throw new ClassCastException(value.getClass().getName() + " cannot be cast to " + type.getName() + ", value: " + value);
        } catch (Exception e) {
            throw new ClassCastException(value.getClass().getName() + " cannot be cast to "
                    + type.getName() + ", value: " + value + ", reason: " + e.getMessage());
        }
    }

    public static boolean safeEquals(Object o1, Object o2, boolean onBothNull) {
        if (o1 == o2) {
            return true;
        }
        if (o1 != null) {
            return o1.equals(o2);
        }
        return false;
    }
}
