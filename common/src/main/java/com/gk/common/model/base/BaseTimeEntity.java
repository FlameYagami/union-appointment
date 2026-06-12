package com.gk.common.model.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author GuoYu
 * @since 2022-11-09 10:39
 **/
@Data
public abstract class BaseTimeEntity implements Serializable {

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 可用状态
     */
    private String enabled;

}
