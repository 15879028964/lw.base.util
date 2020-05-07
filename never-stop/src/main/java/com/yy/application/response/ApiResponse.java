package com.yy.application.response;
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

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author: Daniel Gao
 * @version: v1.0
 * @description:
 * @date: 2020年02月13日 12:45
 */
@Data
@Builder
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = -5789362861961176083L;
    /**
     * 返回码
     */
    private String code;
    /**
     * 数据
     */

    private T data;
    /**
     * 返回附加信息
     */

    private String msg;
    /**
     * 错误信息
     */
    private String errMsg;

    @Tolerate
    public ApiResponse() {
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
