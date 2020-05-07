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

import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: David.liu
 * @version: v1.0
 * @description:线程池工具类
 * @date: 2020年05月6日 10:54
 */
public class ThreadPoolExecutors implements InitializingBean {

    private ExecutorService executorService;

    @Override
    public void afterPropertiesSet() {
        executorService = new ThreadPoolExecutor(10, 100, 3L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000));
    }

    public ExecutorService getExecutor() {
        return executorService;
    }
}
