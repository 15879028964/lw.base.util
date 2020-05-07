package com.yy.application.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2020年05月6日 10:54
 */
@Slf4j
public class ResponseUtils {

    public static void writeJson(HttpServletResponse response, String resultJson) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(resultJson);
            response.getWriter().flush();
        } catch (IOException var4) {
            log.error(var4.getMessage(), var4);
        }
    }

    public static void setNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

}
