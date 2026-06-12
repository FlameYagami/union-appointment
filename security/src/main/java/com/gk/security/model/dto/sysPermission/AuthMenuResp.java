package com.gk.security.model.dto.sysPermission;

import com.gk.common.model.base.BaseTreeResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 菜单信息响应
 *
 * @author Flame
 * @date 2023-02-07 14:16
 **/

@ApiModel("菜单信息响应")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AuthMenuResp extends BaseTreeResp<AuthMenuResp> {

    @ApiModelProperty(value = "菜单名称", example = "菜单名称")
    private String name;

    @ApiModelProperty(value = "路由地址", example = "post", notes = "仅菜单类型为菜单或者目录时,才需要传")
    private String path;

    @ApiModelProperty(value = "组件路径", example = "system/post/index", notes = "仅菜单类型为菜单时才需要传")
    private String component;

    @ApiModelProperty(value = "菜单图标", example = "/menu/list", notes = "仅菜单类型为菜单或者目录时才需要传")
    private String icon;

    @ApiModelProperty(value = "菜单类型", example = "1")
    private String type;

    @ApiModelProperty(value = "链接地址", example = "https://www.baidu.com")
    private String linkUrl;

    @ApiModelProperty(value = "激活名称", example = "system")
    private String activeName;

    @ApiModelProperty(value = "是否隐藏", example = "true")
    private boolean hidden;

    @ApiModelProperty(value = "是否缓存", example = "false")
    private boolean keepAlive;

    public AuthMenuResp(long id, long parentId) {
        super(id, parentId);
    }

}
