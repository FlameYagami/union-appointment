package com.gk.framework.utils;

import com.gk.common.constant.CommonConstant;
import com.gk.common.enums.YesOrNo;

/**
 * 描述工具类
 *
 * @author Flame
 * @date 2023-03-24 18:11
 **/
public class DescUtils {

    /**
     * 将数据状态转换为描述
     *
     * @param dataStatus 数据状态(1:正常, 0:停用)
     */
    public static String getDataStatusDesc(String dataStatus) {
        return YesOrNo.NO.value.equals(dataStatus) ? "(停用)" : CommonConstant.EMPTY;
    }


}
