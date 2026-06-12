package com.gk.openapi.model.base;

import com.gk.common.constant.CommonConstant;
import com.gk.common.utils.ServletExtUtils;
import com.gk.framework.model.bo.system.sysOpenConfig.OpenConfigCache;
import com.gk.openapi.helper.OpenApiHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 第三方接口请求基础模型
 *
 * @author Flame
 * @date 2023-03-14 16:44
 **/
@ApiModel(description = "第三方接口请求基础模型")
@Data
public class BaseOpenApiReq {

    @ApiModelProperty(value = "认证id", hidden = true, example = "e15cf521b83a")
    private String openId;

    @ApiModelProperty(value = "部门id", hidden = true, example = "b476d631c90bd41b98774185079d346b")
    private long deptId;

    @ApiModelProperty(value = "角色id", hidden = true, example = "1000000000000000001")
    private long roleId;

    public void lateInit() {
        // 从请求中获取openId
        this.openId = ServletExtUtils.getRequest().getHeader(CommonConstant.OPEN_ID_HEADER);
        OpenConfigCache cache = OpenApiHelper.getInstance().findOpenConfigCache(openId);
        this.deptId = cache.getDeptId();
        this.roleId = cache.getRoleId();
    }

}
