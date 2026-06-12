package com.gk.framework.model.dto.system.sysOpenConfig;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 第三方对接配置 响应类
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */
@ApiModel(value = "第三方对接配置响应")
@Data
@Accessors(chain = true)
public class SysOpenConfigResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "部门表id", example = "1000000000000000001")
    private long deptId;

    @ApiModelProperty(value = "角色表id", example = "1000000000000000001")
    private long roleId;

    @ApiModelProperty(value = "开放式认证系统id;由系统生成", example = "3b33212edda7")
    private String openId;

    @ApiModelProperty(value = "秘钥;由系统生成", example = "opene326ff91901c")
    private String aesKey;

    @ApiModelProperty(value = "初始化向量;由系统生成", example = "2019876d6ee9e79d")
    private String aesIv;

    @ApiModelProperty(value = "秘钥状态(1:正常, 0:停用)", example = "1")
    private String dataStatus;

    @ApiModelProperty(value = "对接厂商名称", example = "对接厂商名称")
    private String companyName;

    @ApiModelProperty(value = "对接厂商描述", example = "对接厂商描述")
    private String companyDesc;

}
