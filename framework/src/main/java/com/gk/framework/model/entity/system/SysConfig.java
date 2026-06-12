package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.model.dto.system.sysConfig.SysConfigResp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 配置表 数据库模型
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysConfig extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 是否为系统数据(1:是, 0:否, 默认:0)
     */
    private String systemData;

    /**
     * 数据状态(1:正常, 0:停用, 默认:1)
     */
    private String dataStatus;

    /**
     * 备注
     */
    private String remark;


    public SysConfigResp toResp() {
        return new SysConfigResp()
                .setId(id)
                .setConfigName(configName)
                .setConfigKey(configKey)
                .setConfigValue(configValue)
                .setSystemData(systemData)
                .setDataStatus(dataStatus)
                .setRemark(remark);
    }

}
