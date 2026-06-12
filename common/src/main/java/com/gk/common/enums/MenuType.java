package com.gk.common.enums;

import com.gk.common.intf.EnumValuable;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单类型
 *
 * @author Flame
 * @date 2023-02-08 14:24
 **/
@Getter
public enum MenuType implements EnumValuable {

    /**
     * 目录
     */
    CATALOG("1"),

    /**
     * 菜单
     */
    MENU("2"),

    /**
     * 按钮
     */
    BUTTON("3");

    public final String value;

    MenuType(String value) {
        this.value = value;
    }

    public static List<String> getValues(Collection<MenuType> menuTypes) {
        return menuTypes.stream()
                .map(it -> it.value)
                .collect(Collectors.toList());
    }
}
