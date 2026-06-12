package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gk.common.model.base.BaseTimeEntity;
import com.gk.framework.model.dto.system.sysExceptionLog.SysExceptionLogResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 异常日志表 数据库模型
 *
 * @author GuoYu
 * @since 2024-05-15 15:16:18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_exception_log")
public class SysExceptionLog extends BaseTimeEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 异常类型(1:暴力登录, 2:异地登录, 3:账号锁定, 4:密码变更)
     */
    private String exceptionType;

    /**
     * ip地址
     */
    private String ip;

    /**
     * ip归属地
     */
    private String location;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;


    public SysExceptionLogResp toResp() {
        return new SysExceptionLogResp()
                .setId(id)
                .setUsername(username)
                .setExceptionType(exceptionType)
                .setIp(ip)
                .setLocation(location)
                .setBrowser(browser)
                .setOs(os);
    }

}
