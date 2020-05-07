package com.yy.application.exception;
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

import lombok.Getter;
import lombok.Setter;

/**
 * @author: David.liu
 * @version: v1.0
 * @description: 全局基本异常，所有的项目中的业务异常都需要继承这个类
 * @date: 2020年04月11日 13:49
 */
@Getter
@Setter
public abstract class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;

    private String message;

}
