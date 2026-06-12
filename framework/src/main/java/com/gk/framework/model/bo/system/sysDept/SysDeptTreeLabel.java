package com.gk.framework.model.bo.system.sysDept;

import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.utils.DescUtils;
import lombok.Data;

/**
 * 树形部门
 *
 * @author Flame
 * @date 2023-02-03 15:18
 **/

@Data
public class SysDeptTreeLabel {

    // 部门id
    private long id;

    // 父级部门id
    private long parentId;

    // 部门名称
    private String deptName;

    // 部门等级
    private int deptLevel;

    // 部门状态(1:正常, 0:停用)
    private String dataStatus;

    public TreeLabelResp toTreeLabelResp() {
        return new TreeLabelResp(id, parentId, dataStatus)
                .setName(deptName + DescUtils.getDataStatusDesc(dataStatus));
    }

}
