package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseTimeEntity;
import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.framework.model.dto.system.sysOpenConfig.SysOpenConfigResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 第三方对接配置表 数据库模型
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysOpenConfig extends BaseTimeEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 部门表id
     */
    private Long deptId;

    /**
     * 角色表id
     */
    private Long roleId;

    /**
     * 开放式认证系统id;由系统生成
     */
    private String openId;

    /**
     * 秘钥;由系统生成
     */
    private String aesKey;

    /**
     * 初始化向量;由系统生成
     */
    private String aesIv;

    /**
     * 秘钥状态(1:正常, 0:停用, 默认:1)
     */
    private String dataStatus;

    /**
     * 对接厂商名称
     */
    private String companyName;

    /**
     * 对接厂商描述
     */
    private String companyDesc;


    public SysOpenConfigResp toResp() {
        return new SysOpenConfigResp()
                .setId(id)
                .setDeptId(deptId)
                .setRoleId(roleId)
                .setOpenId(openId)
                .setAesKey(aesKey)
                .setAesIv(aesIv)
                .setDataStatus(dataStatus)
                .setCompanyName(companyName)
                .setCompanyDesc(companyDesc);
    }

    public OpenConfigCache toCache() {
        return new OpenConfigCache()
                .setDeptId(deptId)
                .setRoleId(roleId)
                .setOpenId(openId)
                .setAesKey(aesKey)
                .setAesIv(aesIv);
    }


}
