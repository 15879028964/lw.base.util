
package com.yy.application.util;

import java.util.Date;

/**
 * 常用的日期模式.
 */
public final class DatePattern {

    //---------------------------------------------------------------


    public static final String COMMON_DATE                                  = "yyyy-MM-dd";
    public static final String COMMON_DATE_AND_TIME_WITHOUT_SECOND          = "yyyy-MM-dd HH:mm";
    public static final String COMMON_DATE_AND_TIME                         = "yyyy-MM-dd HH:mm:ss";
    public static final String COMMON_DATE_AND_TIME_WITH_MILLISECOND        = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DOTS_DATE                                    = "yyyy.MM.dd";
    public static final String DOTS_DATE_AND_TIME_WITHOUT_SECOND            = "yyyy.MM.dd HH:mm";
    public static final String DOTS_DATE_AND_TIME                           = "yyyy.MM.dd HH:mm:ss";
    public static final String DOTS_DATE_AND_TIME_WITH_MILLISECOND          = "yyyy.MM.dd HH:mm:ss.SSS";


    public static final String COMMON_TIME_WITHOUT_SECOND                   = "HH:mm";
    public static final String COMMON_TIME                                  = "HH:mm:ss";
    public static final String HH                                           = "HH";


    public static final String COMMON_DATE_AND_TIME_WITHOUT_YEAR_AND_SECOND = "MM-dd HH:mm";
    public static final String MM                                           = "MM";
    public static final String MONTH_AND_DAY                                = "MM-dd";
    public static final String MONTH_AND_DAY_WITH_WEEK                      = "MM-dd(E)";



    public static final String TIMESTAMP                                    = "yyyyMMddHHmmss";
    public static final String TIMESTAMP_WITH_MILLISECOND                   = "yyyyMMddHHmmssSSS";
    public static final String YEAR_AND_MONTH                               = "yyyy-MM";
    public static final String BASIC_ISO_DATE                               = "yyyyMMdd";
    public static final String CHINESE_DATE                                 = "yyyy年MM月dd日";
    public static final String CHINESE_DATE_AND_TIME                        = "yyyy年MM月dd日 HH:mm:ss";


    public static final String INDONESIA_DATE                               = "dd/MM/yyyy";
    public static final String INDONESIA_DATE_AND_TIME                      = "dd/MM/yyyy HH:mm:ss";



    public static final String TO_STRING_STYLE                              = "EEE MMM dd HH:mm:ss zzz yyyy";

    private DatePattern(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
}
