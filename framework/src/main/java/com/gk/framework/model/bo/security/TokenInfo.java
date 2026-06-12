package com.gk.framework.model.bo.security;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Token内置信息类
 *
 * @author GuoYu
 * @since 2023-01-13 15:30
 **/
@Data
@Accessors(chain = true)
public class TokenInfo {
    /**
     * 用户id
     */
    private long userId;
    /**
     * 时间戳
     */
    private long timestamp;
}
