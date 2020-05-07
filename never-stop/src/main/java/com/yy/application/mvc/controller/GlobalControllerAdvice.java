package com.yy.application.mvc.controller;
import com.yy.application.exception.BaseException;
import com.yy.application.response.ApiResponse;
import com.yy.application.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: David.liu
 * @version: v1.0
 * @description: 全局的异常处理器，所有的业务异常继承BaseException然后在这里处理
 * ApiResponse的返回值中，errMsg字段不允许把堆栈信息以及程序报错信息直接暴露，当前仅允许
 * BaseException的内容被暴露，因为是自定义的Message
 * 其余的异常统一用系统异常或者参数异常暴露，坚决不允许直接暴露程序相关信息（IllegalArgumentException），
 * <p>不要为了查看方便而在前台展示内部信息</p>
 * @date: 2020年04月11日 13:48
 */
@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    private static final String ERROR_CODE_PARAM = "101";
    private static final String ERROR_CODE_COMMON = "-1";
    private static final String ERROR_MSG_COMMON = "系统异常";
    private static final String ERROR_PARAM_COMMON = "参数异常";

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> processException(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException e) {
        log.error("params exception occurs", e);
        ResponseUtils.setNoCache(response);
        ApiResponse<?> result = new ApiResponse<>();
        result.setCode(ERROR_CODE_PARAM);
        result.setErrMsg(e.getMessage());
        result.setMsg(ERROR_PARAM_COMMON);
        return result;
    }

    @ExceptionHandler(BaseException.class)
    public ApiResponse<?> processException(HttpServletRequest request, HttpServletResponse response, BaseException e) {
        log.error("base exception occurs", e);
        ResponseUtils.setNoCache(response);
        ApiResponse<?> result = new ApiResponse<>();
        result.setCode(e.getCode());
        result.setErrMsg(e.getMessage());
        return result;
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ApiResponse<?> processException(HttpServletRequest request, HttpServletResponse response, HttpMessageConversionException e) {
        log.error("http message exception occurs", e);
        ResponseUtils.setNoCache(response);
        ApiResponse<?> result = new ApiResponse<>();
        result.setCode(ERROR_CODE_COMMON);
        result.setErrMsg(ERROR_PARAM_COMMON);
        return result;
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> processException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("exception occurs", e);
        ResponseUtils.setNoCache(response);
        ApiResponse<?> result = new ApiResponse<>();
        result.setCode(ERROR_CODE_COMMON);
        result.setErrMsg(ERROR_MSG_COMMON);
        return result;
    }

    @ExceptionHandler(Throwable.class)
    public ApiResponse<?> processException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        log.error("throwable error", e);
        ResponseUtils.setNoCache(response);
        ApiResponse<?> result = new ApiResponse<>();
        result.setCode(ERROR_CODE_COMMON);
        result.setErrMsg(ERROR_MSG_COMMON);
        return result;
    }
}
