package com.yy.application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年03月20日 11:08
 */
public class SafeSimpleDateFormat {
    private final String _format;

    private static final ThreadLocal dateFormats = ThreadLocal.withInitial(HashMap::new);

    private SimpleDateFormat getDateFormat(String format) {
        Map<String, SimpleDateFormat> formatters = (Map) dateFormats.get();
        SimpleDateFormat formatter = formatters.get(format);
        if (formatter == null) {
            formatter = new SimpleDateFormat(format);
            formatters.put(format, formatter);
        }
        return formatter;
    }

    public SafeSimpleDateFormat(String format) {
        _format = format;
    }

    public String format(Date date) {
        return getDateFormat(_format).format(date);
    }

    public String format(Object date) {
        return getDateFormat(_format).format(date);
    }

    public Date parse(String day) throws ParseException {
        return getDateFormat(_format).parse(day);
    }
}
