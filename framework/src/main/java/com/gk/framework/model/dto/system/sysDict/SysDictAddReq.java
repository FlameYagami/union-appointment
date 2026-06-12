package com.gk.framework.model.dto.system.sysDict;

import com.gk.framework.model.entity.system.SysDict;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典 新增请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictAddReq extends SysDictBaseReq {

    @Override
    public SysDict toEntity() {
        return super.toEntity();
    }

}
