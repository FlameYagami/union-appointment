package com.gk.common.model.dto;

import com.gk.common.enums.DataStatus;
import com.gk.common.model.base.BaseTreeResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 树形标签响应
 *
 * @author Flame
 * @date 2023-02-03 15:18
 **/

@ApiModel("树形结构标签响应")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TreeLabelResp extends BaseTreeResp<TreeLabelResp> {

    @ApiModelProperty(value = "节点名称", example = "1")
    private String name;

    public TreeLabelResp(long id, long parentId) {
        super(id, parentId, false);
    }

    public TreeLabelResp(long id, long parentId, String dataStatus) {
        super(id, parentId, DataStatus.DISABLE.value.equals(dataStatus));
    }

}
