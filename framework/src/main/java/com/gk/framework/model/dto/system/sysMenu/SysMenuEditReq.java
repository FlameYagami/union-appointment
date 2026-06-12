package com.gk.framework.model.dto.system.sysMenu;

import com.gk.common.enums.MenuType;
import com.gk.framework.model.entity.system.SysMenu;
import com.gk.framework.validate.InEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 菜单权限 修改请求类
 *
 * @author Flame
 * @since 2022-12-29 09:30:38
 */
@ApiModel(value = "菜单权限修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysMenuEditReq extends SysMenuBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @ApiModelProperty(value = "父级id", required = true, example = "1000000000000000000")
    @Min(value = 1000000000000000000L, message = "id不合法")
    private long parentId;

    @ApiModelProperty(value = "菜单类型(1:目录, 2:菜单, 3:按钮)", required = true, example = "1")
    @NotBlank(message = "菜单类型不能为空")
    @InEnum(MenuType.class)
    private String menuType;

    public SysMenu toEntity(String parentMenuOpen) {
        return super.toEntity(parentId, menuType, parentMenuOpen)
                .setId(id);
    }

}
