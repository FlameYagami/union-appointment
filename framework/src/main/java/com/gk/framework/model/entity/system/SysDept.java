package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.enums.DataStatus;
import com.gk.common.model.base.BaseEntity;
import com.gk.common.model.dto.LabelResp;
import com.gk.common.model.dto.TreeLabelResp;
import com.gk.framework.model.dto.system.sysDept.SysDeptResp;
import com.gk.framework.utils.DescUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 部门表 数据库模型
 *
 * @author Flame
 * @since 2022-12-28 17:10:44
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysDept extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门层级
     */
    private Integer deptLevel;

    /**
     * 部门类型
     */
    private String deptType;

    /**
     * 部门状态
     */
    private String dataStatus;

    /**
     * 部门排序
     */
    private Integer deptOrder;


    public SysDeptResp toResp() {
        return new SysDeptResp()
                .setId(id)
                .setParentId(parentId)
                .setDeptName(deptName)
                .setDeptCode(deptCode)
                .setDeptLevel(deptLevel)
                .setDeptType(deptType)
                .setDataStatus(dataStatus)
                .setDeptOrder(deptOrder);
    }

    public LabelResp toLabelResp() {
        return new LabelResp()
                .setId(id)
                .setName(deptName + DescUtils.getDataStatusDesc(dataStatus))
                .setDisabled(DataStatus.DISABLE.value.equals(dataStatus));
    }

    public TreeLabelResp toTreeLabelResp() {
        return new TreeLabelResp(id, parentId, dataStatus)
                .setName(deptName + DescUtils.getDataStatusDesc(dataStatus));
    }

}
