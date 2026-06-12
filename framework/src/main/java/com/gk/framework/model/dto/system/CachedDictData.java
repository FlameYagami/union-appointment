package com.gk.framework.model.dto.system;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 字典缓存类
 *
 * @author GuoYu
 * @since 2023-02-16 10:23
 **/

@Data
@Accessors(chain = true)
public class CachedDictData {

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
}
