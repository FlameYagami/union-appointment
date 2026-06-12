package com.gk.callapi.demo.model;

import lombok.Data;

/**
 * 示例响应
 *
 * @author Flame
 * @date 2023-04-23 16:19
 **/
@Data
public class DemoResp<T> {

    /**
     * 响应状态
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

}
