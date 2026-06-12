package com.gk.common.utils;

import com.gk.common.model.base.BaseTreeResp;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 *
 * @author Flame
 * @date 2023-02-03 15:24
 **/
public class TreeUtils {

    private TreeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T extends BaseTreeResp<T>> List<T> buildTree(List<T> resp) {
        // 获取所有的id
        List<Long> allIds = resp.stream()
                .map(BaseTreeResp::getId)
                .collect(Collectors.toList());
        // 索引构建树状结构
        Map<Long, List<T>> groupMap = resp.stream()
                .collect(Collectors.groupingBy(BaseTreeResp::getParentId, LinkedHashMap::new, Collectors.toList()));
        // 使用引用设置子节点
        resp.forEach(tree -> tree.setChildren(groupMap.get(tree.getId())));
        // 查询所有的顶级节点
        return resp.stream()
                .filter(it -> !allIds.contains(it.getParentId()))
                .collect(Collectors.toList());
    }

}
