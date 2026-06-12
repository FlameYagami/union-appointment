package com.gk.framework.model.dto.system.sysDictData;

import com.gk.framework.model.entity.system.SysDictData;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据 新增请求类
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@ApiModel(value = "字典数据新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictDataAddReq extends SysDictDataBaseReq {

    @Override
    public SysDictData toEntity() {
        return super.toEntity();
    }

}
