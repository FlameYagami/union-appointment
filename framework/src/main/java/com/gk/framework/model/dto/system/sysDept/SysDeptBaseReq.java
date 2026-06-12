package com.gk.framework.model.dto.system.sysDept;

import com.gk.common.enums.DataStatus;
import com.gk.common.enums.DeptType;
import com.gk.framework.model.entity.system.SysDept;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 部门 基础请求类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
@Data
public class SysDeptBaseReq {

    @ApiModelProperty(value = "父级id", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long parentId;

    @ApiModelProperty(value = "部门名称[32]", required = true, example = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 32, message = "部门名称最多输入32个字符")
    private String deptName;

    @ApiModelProperty(value = "部门编码[64]", required = true, example = "部门编码")
    @Length(max = 64, message = "部门编码最多输入64个字符")
    private String deptCode;

    @ApiModelProperty(value = "部门状态(1:正常, 0:停用)", required = true, example = "1")
    @NotBlank(message = "部门状态不能为空")
    @InEnum(DataStatus.class)
    private String dataStatus;

    @ApiModelProperty(value = "部门排序", required = true, example = "100")
    @Min(value = 1, message = "部门排序不合法")
    private int deptOrder;

    /**
    * 转换成数据库操作类
    */
    protected SysDept toEntity(int deptLevel) {
        return new SysDept()
                .setParentId(parentId)
                .setDeptName(deptName)
                .setDeptCode(deptCode)
                .setDeptType(DeptType.DEPT.value) // 部门的管理页面默认为部门类型的数据
                .setDeptLevel(deptLevel)
                .setDataStatus(dataStatus)
                .setDeptOrder(deptOrder);
    }

}
