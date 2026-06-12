package com.gk.framework.model.dto.system.sysDict;

import com.gk.framework.model.entity.system.SysDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 字典 修改请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictEditReq extends SysDictBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @Override
    public SysDict toEntity() {
        return super.toEntity()
                .setId(id);
    }

}
