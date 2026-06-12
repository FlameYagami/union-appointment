package com.gk.common.intf;

/**
 * 异常枚举接口类(每一个业务模块都需要创建一个XxxStatus, 并实现此接口)
 *
 * @author Flame
 * @date 2023-05-10 8:45
 **/
public interface IResultStatus {

    /**
     * 返回码
     */
    int getCode();

    /**
     * 返回结果描述
     */
    String getMessage();


}
