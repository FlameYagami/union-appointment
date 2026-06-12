package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.model.dto.system.sysDict.SysDictResp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典表 数据库模型
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysDict extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典备注
     */
    private String remark;


    public SysDictResp toResp() {
        return new SysDictResp()
                .setId(id)
                .setDictName(dictName)
                .setDictCode(dictCode)
                .setRemark(remark);
    }

}
