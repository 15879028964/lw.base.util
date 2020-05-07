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
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:
 * @date: 2019年12月30日 22:56
 */
public class RequestUtils {

    public static boolean isAjax(HttpServletRequest request) {
        return request != null && ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With")) || getAcceptedMediaTypes(request).contains(MediaType.APPLICATION_JSON));
    }

    private static List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
        Object acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        return (List) acceptedMediaTypes;
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        String result = request.getParameter(cookieName);
        if (StringUtils.isEmpty(result)) {
            Cookie[] cks = request.getCookies();
            if (cks != null) {
                for (Cookie cookie : cks) {
                    if (cookieName.equals(cookie.getName())) {
                        result = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static String getUserIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 获取客户端前台IP进行解析
        String UserIp = request.getHeader("x-forwarded-for");
        if (UserIp == null || UserIp.length() == 0 || "unknown".equalsIgnoreCase(UserIp)) {
            UserIp = request.getHeader("Proxy-Client-IP");
        }
        if (UserIp == null || UserIp.length() == 0 || "unknown".equalsIgnoreCase(UserIp)) {
            UserIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (UserIp == null || UserIp.length() == 0 || "unknown".equalsIgnoreCase(UserIp)) {
            UserIp = request.getRemoteAddr();
        }
        return UserIp;
    }

    /**
     * 从Request对象中获得客户端IP，处理了HTTP代理服务器和Nginx的反向代理截取了ip
     *
     * @param request
     * @return ip
     */
    public static String getLocalIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");
        String ip;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded.split(",")[0];
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                if (forwarded != null) {
                    forwarded = forwarded.split(",")[0];
                }
                ip = realIp + "/" + forwarded;
            }
        }
        return ip;
    }

}