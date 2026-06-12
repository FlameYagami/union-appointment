package com.gk.framework.model.dto.system.sysDict;

import com.gk.framework.model.entity.system.SysDict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 字典 基础请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@Data
public class SysDictBaseReq {

    @ApiModelProperty(value = "字典名称[32]", required = true, example = "字典名称")
    @NotBlank(message = "字典名称不能为空")
    @Length(max = 32, message = "字典名称最多输入32个字符")
    private String dictName;

    @ApiModelProperty(value = "字典编码[64]", required = true, example = "字典编码")
    @NotBlank(message = "字典编码不能为空")
    @Length(max = 64, message = "字典编码最多输入64个字符")
    private String dictCode;

    @ApiModelProperty(value = "字典备注[255]", example = "字典备注")
    @Length(max = 255, message = "字典备注最多输入255个字符")
    private String remark;

    /**
    * 转换成数据库操作类
    */
    protected SysDict toEntity() {
        return new SysDict()
                .setDictName(dictName)
                .setDictCode(dictCode)
                .setRemark(remark);
    }

}
