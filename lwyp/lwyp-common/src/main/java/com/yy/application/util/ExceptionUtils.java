package com.yy.application.util;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
public class ExceptionUtils {

    public static RuntimeException wrap2Runtime(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }

    /**
     * 得到包装类型异常的原始异常
     *
     * @param e
     * @return
     * @author
     * @CreateDate 2011-11-28 下午02:07:39
     */
    public static Throwable getOriginal(Throwable e) {
        Throwable ex = e.getCause();
        if (ex != null) {
            return getOriginal(ex);
        } else {
            return e;
        }
    }
}
