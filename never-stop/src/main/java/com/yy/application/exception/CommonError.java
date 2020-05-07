package com.yy.application.exception;

/**
 * @Description: 公共错误接口
 * @Author: David.liu
 * @Date: 2020/2/20 11:01
 */
public interface CommonError {

    /**
     * 获取错误码
     *
     * @return
     */
    String getErrCode();


    /**
     * 获取错误信息
     *
     * @return
     */
    String getErrMsg();


    /**
     * 动态设置错误信息，并返回错误实体类
     *
     * @param errMsg
     * @return
     */
    CommonError setErrMsg(String errMsg);

    /**
     * 动态设置错误信息，并返回错误实体类
     *
     * @param errCode
     * @return
     */
    CommonError setErrCode(String errCode);

}
