package com.yy.application.mvc.controller;
import com.yy.application.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * @author: Daniel Gao
 * @version: v1.0
 * @description:
 * @date: 2020年04月13日 10:17
 */
@Slf4j
public class BaseAction {

    protected static final String SUCCESS = "0";
    protected static final String FAIL = "-1";

    public <T> ApiResponse<T> returnSuccess(T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(SUCCESS);
        apiResponse.setMsg("success");
        apiResponse.setData(data);
        return apiResponse;
    }

    public <T> ApiResponse<T> returnError(T data, String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(FAIL);
        apiResponse.setErrMsg(message);
        apiResponse.setData(data);
        apiResponse.setMsg("fail");
        return apiResponse;
    }

    protected <T> ApiResponse<T> generateJsonResult(String code, String msg, T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMsg(msg);
        apiResponse.setData(data);
        return apiResponse;
    }

    /*参数绑定处理*/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
