package com.gk.common.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 基础树形结构响应
 *
 * @author Flame
 * @date 2023-02-03 15:18
 **/

@ApiModel("基础树形结构响应")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class BaseTreeResp<T> {

    @ApiModelProperty(value = "节点id", example = "1")
    private long id;

    @ApiModelProperty(value = "父级节点id", example = "1")
    private long parentId;

    @ApiModelProperty(value = "节点是否禁用", example = "false")
    private boolean disabled;

    private List<T> children;

    public BaseTreeResp(long id, long parentId) {
        this.id = id;
        this.parentId = parentId;
        this.disabled = false;
    }

    public BaseTreeResp(long id, long parentId, boolean disabled) {
        this.id = id;
        this.parentId = parentId;
        this.disabled = disabled;
    }

}
