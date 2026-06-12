package com.gk.framework.model.dto.system.sysDictData;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 字典数据 批量删除请求类
 *
 * @author GuoYu
 * @since 2023-02-17 13:52
 **/
@ApiModel(value = "字典数据批量删除请求")
@Data
public class SysDictDataDeleteReq {

    @ApiModelProperty(value = "字典编码", required = true, example = "字典编码")
    @NotBlank(message = "字典编码缺失")
    private String dictCode;

    @ApiModelProperty(value = "字典项id集合", required = true)
    @NotEmpty(message = "字典项id缺失")
    private List<@Min(value = 1000000000000000001L, message = "id不合法") Long> idList;
}
