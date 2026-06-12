package com.gk.framework.model.dto.system.sysDept;

import com.gk.framework.model.entity.system.SysDept;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门 新增请求类
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
@ApiModel(value = "部门新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptAddReq extends SysDeptBaseReq {

    @Override
    public SysDept toEntity(int deptLevel) {
        return super.toEntity(deptLevel);
    }

}
