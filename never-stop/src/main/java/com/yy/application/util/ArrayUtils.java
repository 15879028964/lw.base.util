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

import java.lang.reflect.Array;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
public class ArrayUtils {

    public static <T> boolean contains(T[] array, T t) {
        for (T item : array) {
            if (t.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T safeGet(T[] array, int idx) {
        if (array.length > idx) {
            return array[idx];
        }
        return null;
    }

    public static boolean isEmpty(Object[] objs) {
        return objs == null || objs.length == 0;
    }

    public static <T> String join(T[] arrayObj, String split) {
        return join(arrayObj, split, "");
    }

    public static <T> String join(T[] arrayObj, String split, String quote) {
        return join(arrayObj, split, quote, quote);
    }

    public static <T> String join(T[] arrayObj, String split, String leftQuote, String rightQuote) {
        if (arrayObj == null) {
            return null;
        }

        if (leftQuote == null) {
            leftQuote = "";
        }
        if (rightQuote == null) {
            rightQuote = "";
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arrayObj.length; i++) {
            if (i > 0) {
                sb.append(split);
            }
            sb.append(leftQuote);
            sb.append(arrayObj[i]);
            sb.append(rightQuote);
        }
        return sb.toString();
    }

    public static <T, R> R[] map(T[] array, Class<R> rsltArrayClass) {
        R[] rsltArray = (R[]) Array.newInstance(rsltArrayClass, array.length);
        for (int i = 0; i < array.length; i++) {
            rsltArray[i] = ValueUtils.convert(array[i], rsltArrayClass);
        }
        return rsltArray;
    }

    /**
     * 分隔处理一个大的数组
     *
     * @param array
     * @param splitSize
     * @param callback
     * @throws Exception
     */
    public static <T> void split(T[] array, int splitSize, ArraySplitCallback<T> callback) throws Exception {
        int left = array.length; // 剩下的
        int idx = 0;
        int batch = 1;

        while (left > 0) {
            int splitLength = splitSize;
            if (left < splitSize) splitLength = left;

            Object[] splitValues = new Object[splitLength];
            System.arraycopy(array, idx, splitValues, 0, splitLength);

            callback.onSplitValue((T[]) splitValues, batch++);

            idx += splitLength;
            left = left - splitLength;
        }
    }

    public interface ArraySplitCallback<T> {
        void onSplitValue(T[] splitValues, int batch) throws Exception;
    }

}
