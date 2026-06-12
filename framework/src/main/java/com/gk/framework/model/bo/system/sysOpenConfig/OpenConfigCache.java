package com.gk.framework.model.bo.system.sysOpenConfig;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 第三方对接配置缓存
 *
 * @author Flame
 * @date 2023-03-15 11:04
 **/
@Data
@Accessors(chain = true)
public class OpenConfigCache {

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


}
