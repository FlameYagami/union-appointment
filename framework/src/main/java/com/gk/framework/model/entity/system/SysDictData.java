package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.model.dto.system.CachedDictData;
import com.gk.framework.model.dto.system.sysDictData.SysDictDataResp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 字典数据表 数据库模型
 *
 * @author GuoYu
 * @since 2023-02-13 17:11:51
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysDictData extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典标签
     */
    private String dictLabel;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 颜色类型
     */
    private String colorType;

    /**
     * 字典样式
     */
    private String dictCss;

    /**
     * 字典排序
     */
    private Integer dictOrder;

    /**
     * 字典数据状态(1:正常, 0:停用, 默认:1)
     */
    private String dataStatus;


    public SysDictDataResp toResp() {
        return new SysDictDataResp()
                .setId(id)
                .setDictCode(dictCode)
                .setDictLabel(dictLabel)
                .setDictValue(dictValue)
                .setColorType(colorType)
                .setDictCss(dictCss)
                .setDictOrder(dictOrder)
                .setDataStatus(dataStatus);
    }

    /**
     * 转换为缓存类
     */
    public CachedDictData toCache() {
        return new CachedDictData()
                .setDictLabel(dictLabel)
                .setDictValue(dictValue)
                .setColorType(colorType)
                .setDictCss(dictCss);
    }

}
