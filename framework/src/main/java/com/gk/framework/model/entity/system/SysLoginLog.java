package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gk.common.model.base.BaseTimeEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Flame
 * @since 2022-11-09 10:39
 **/
@Data
@Accessors(chain = true)
@TableName("sys_login_log")
public class SysLoginLog extends BaseTimeEntity {

    @TableId
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 返回消息
     */
    private String resultMsg;

    /**
     * 登录状态 0:失败 1:成功
     */
    private String status;

}
