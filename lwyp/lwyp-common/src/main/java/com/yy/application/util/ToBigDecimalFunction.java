package com.yy.application.util;

import java.math.BigDecimal;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T> {

    BigDecimal applyAsBigDecimal(T value);

}
