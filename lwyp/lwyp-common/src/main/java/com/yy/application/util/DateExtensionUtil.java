package com.yy.application.util;
import org.apache.commons.lang3.Validate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import static com.yy.application.util.TimeInterval.*;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;

/**
 * @author David.liu
 * @description 日期拓展工具类--用于个性化输出结果,针对业务个性化显示.
 * @date 2020/5/7 20:39
 */

public final class DateExtensionUtil {

    //---------------------------------------------------------------

    /** 天. */
    private static final String DAY         = "天";

    /** 小时. */
    private static final String HOUR        = "小时";

    /** 分钟. */
    private static final String MINUTE      = "分钟";

    /** 秒. */
    private static final String SECOND      = "秒";

    /** 毫秒. */
    private static final String MILLISECOND = "毫秒";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private DateExtensionUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }


    /**
     * 获取时间差,最后以中文的形式返回： 1小时30分钟30秒500毫秒
     * @param beginDate
     * @return
     */
    public static String formatDuration(LocalDateTime beginDate){
        return formatDuration(beginDate, LocalDateTime.now());
    }



    /**
     * 获取时间差--以中文形式展示
     * @param beginDate
     * @param endDate
     * @return
     */
    public static String formatDuration(LocalDateTime beginDate,LocalDateTime endDate){
        return formatDuration(getIntervalTime(beginDate, endDate));
    }

    /**
     * 获取时间差--以 H:mm:ss.SSS展示
     * @param beginDate
     * @param endDate
     * @return
     */
    public static String formatDurationForHMS(LocalDateTime beginDate,LocalDateTime endDate){
        return formatDurationHMS(getIntervalTime(beginDate,endDate));
    }


    /**
     * 将间隔毫秒数 <code>spaceMilliseconds</code>,格式化成直观的表示方式.
     * @param spaceMilliseconds
     * @return
     */
    public static String formatDuration(long spaceMilliseconds){
        Validate.isTrue(spaceMilliseconds >= 0, "spaceMilliseconds can't <0");

        if (0 == spaceMilliseconds){
            return "0";
        }
        //---------------------------------------------------------------

        // 间隔天数
        long spaceDay = getIntervalDay(spaceMilliseconds);
        // 间隔小时 减去间隔天数后,
        long spaceHour = getIntervalHour(spaceMilliseconds) - spaceDay * 24;
        // 间隔分钟 减去间隔天数及间隔小时后,
        long spaceMinute = getIntervalMinute(spaceMilliseconds) - (spaceDay * 24 + spaceHour) * 60;
        // 间隔秒 减去间隔天数及间隔小时,间隔分钟后,
        long spaceSecond = getIntervalSecond(spaceMilliseconds) - ((spaceDay * 24 + spaceHour) * 60 + spaceMinute) * 60;
        // 间隔毫秒 减去间隔天数及间隔小时,间隔分钟,间隔秒后,
        long spaceMillisecond = spaceMilliseconds - (((spaceDay * 24 + spaceHour) * 60 + spaceMinute) * 60 + spaceSecond) * 1000;

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();
        if (0 != spaceDay){
            sb.append(spaceDay + DAY);
        }
        if (0 != spaceHour){
            sb.append(spaceHour + HOUR);
        }
        if (0 != spaceMinute){
            sb.append(spaceMinute + MINUTE);
        }
        if (0 != spaceSecond){
            sb.append(spaceSecond + SECOND);
        }
        if (0 != spaceMillisecond){
            sb.append(spaceMillisecond + MILLISECOND);
        }
        return sb.toString();
    }


    /**
     * 两个时间相差的天数.
     * @param spaceMilliseconds
     * @return
     */
    static int getIntervalDay(long spaceMilliseconds){
        return (int) (spaceMilliseconds / (MILLISECOND_PER_DAY));
    }


    /**
     * 两个时间相差的的小时数
     * @param date1
     * @param date2
     * @return
     */
    public static int getIntervalHour(LocalDateTime date1,LocalDateTime date2){
        return getIntervalHour(getIntervalTime(date1, date2));
    }

    /**
     * 相差的小时数
     * @param spaceMilliseconds
     * @return
     */
    static int getIntervalHour(long spaceMilliseconds){
        return (int) (spaceMilliseconds / (MILLISECOND_PER_HOUR));
    }

    /**
     *
     * @param date1 开始时间
     * @param date2 结束时间
     * @return 间隔的分钟
     */
    public static int getIntervalMinute(LocalDateTime date1,LocalDateTime date2){
        return getIntervalMinute(getIntervalTime(date1, date2));
    }

    /**
     * 两个时间相差的分钟.
     * @param spaceMilliseconds
     * @return 相差的分钟
     */
    static int getIntervalMinute(long spaceMilliseconds){
        return (int) (spaceMilliseconds / (MILLISECOND_PER_MINUTE));
    }

    /**
     * 两个时间相差的秒数
     * @param date1 开始时间
     * @param date2 结束时间
     * @return
     */
    public static int getIntervalSecond(LocalDateTime date1,LocalDateTime date2){
        return getIntervalSecond(getIntervalTime(date1, date2));
    }

    /**
     * 两个时间相差的秒数.
     * @param spaceMilliseconds
     * @return
     */
    static int getIntervalSecond(long spaceMilliseconds){
        return (int) (spaceMilliseconds / MILLISECOND_PER_SECONDS);
    }

    /**
     * 两个时间相差的毫秒数
     * @param date1
     * @param date2
     * @return
     */
    public static long getIntervalTime(LocalDateTime date1, LocalDateTime date2){
        Validate.notNull(date1, "date1 can't be null!");
        Validate.notNull(date2, "date2 can't be null!");
        return Math.abs(date1.toInstant(ZoneOffset.of("+8")).toEpochMilli() - date2.toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }


}
