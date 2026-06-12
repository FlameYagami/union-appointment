package com.gk.server.constant;

import com.gk.common.constant.CommonConstant;

/**
 * 文件路径常量类
 *
 * @author Flame
 * @date 2023-02-28 11:37
 **/
public class PathConstant {

    private PathConstant() {
        throw new IllegalStateException("Constant class");
    }

    /**
     * 头像路径
     */
    public static final String AVATAR = "avatar" + CommonConstant.SLASH;

    /**
     * 公告图片路径
     */
    public static final String NOTICE = "notice" + CommonConstant.SLASH;

}
