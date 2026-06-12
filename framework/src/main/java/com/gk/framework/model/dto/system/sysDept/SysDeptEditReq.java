package com.gk.framework.model.dto.system.sysDept;

import com.gk.framework.model.entity.system.SysDept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 部门 修改请求类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
@ApiModel(value = "部门修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptEditReq extends SysDeptBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @Override
    public SysDept toEntity(int deptLevel) {
        return super.toEntity(deptLevel)
                .setId(id);
    }

}
