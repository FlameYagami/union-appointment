package com.gk.framework.model.dto.system.sysOpenConfig;

import com.gk.common.enums.DataStatus;
import com.gk.framework.model.entity.system.SysOpenConfig;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 第三方对接配置 基础请求类
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */
@Data
public class SysOpenConfigBaseReq {

    @ApiModelProperty(value = "部门表id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long deptId;

    @ApiModelProperty(value = "角色表id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000002L, message = "id不合法") // 超管角色校验
    private long roleId;

    @ApiModelProperty(value = "对接厂商名称[32]", required = true, example = "对接厂商名称")
    @NotBlank(message = "对接厂商名称不能为空")
    @Length(max = 32, message = "对接厂商名称最多输入32个字符")
    private String companyName;

    @ApiModelProperty(value = "对接厂商描述[800]", required = true, example = "对接厂商描述")
    @NotBlank(message = "对接厂商描述不能为空")
    @Length(max = 800, message = "对接厂商描述最多输入800个字符")
    private String companyDesc;

    @ApiModelProperty(value = "秘钥状态(1:正常, 0:停用)", required = true, example = "1")
    @NotBlank(message = "秘钥状态不能为空")
    @InEnum(DataStatus.class)
    private String dataStatus;

    /**
    * 转换成数据库操作类
    */
    protected SysOpenConfig toEntity() {
        return new SysOpenConfig()
                .setDeptId(deptId)
                .setRoleId(roleId)
                .setCompanyName(companyName)
                .setCompanyDesc(companyDesc)
                .setDataStatus(dataStatus);
    }

}
