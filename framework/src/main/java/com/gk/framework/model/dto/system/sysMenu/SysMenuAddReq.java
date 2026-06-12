package com.gk.framework.model.dto.system.sysMenu;

import com.gk.common.enums.MenuType;
import com.gk.framework.model.entity.system.SysMenu;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单权限 新增请求类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
@ApiModel(value = "菜单权限新增请求")
@Data
public class SysMenuAddReq {

    @ApiModelProperty(value = "父级id", required = true, example = "1000000000000000000")
    @Min(value = 1000000000000000000L, message = "id不合法")
    private long parentId;

    @ApiModelProperty(value = "菜单类型(1:目录, 2:菜单, 3:按钮)", required = true, example = "1")
    @NotBlank(message = "菜单类型不能为空")
    @InEnum(MenuType.class)
    private String menuType;

    @NotEmpty(message = "参数缺失")
    @Valid
    private List<SysMenuBaseReq> sysMenuBases;

    public List<SysMenu> toEntities(String parentMenuOpen) {
        return sysMenuBases.stream()
                .map(it -> it.toEntity(parentId, menuType, parentMenuOpen))
                .collect(Collectors.toList());
    }

}
