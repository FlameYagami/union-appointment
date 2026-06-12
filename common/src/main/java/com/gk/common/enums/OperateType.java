package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

/**
 * 操作类型
 *
 * @author Flame
 * @date 2023-02-08 14:24
 */
@Getter
public enum OperateType implements EnumValuable {
    /**
     * 新增
     */
    ADD("Add"),

    /**
     * 修改
     */
    UPDATE("Update"),

    /**
     * 删除
     */
    DELETE("Delete"),

    /**
     * 导入
     */
    IMPORT("Import"),

    /**
     * 导出
     */
    EXPORT("Export"),

    /**
     * 其他
     */
    OTHER("Other");


    public final String value;

    OperateType(String value) {
        this.value = value;
    }

}
