package com.gk.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 分页工具类
 *
 * @author GuoYu
 * @since 2023-05-23 10:51
 **/
public class PageUtils {

    private PageUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> IPage<T> create(long total, List<T> list) {
        IPage<T> page = new Page<>();
        page.setTotal(total);
        page.setRecords(list);
        return page;
    }
}
