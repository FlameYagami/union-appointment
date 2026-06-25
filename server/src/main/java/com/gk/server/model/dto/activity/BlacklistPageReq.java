package com.gk.server.model.dto.activity;

import com.gk.common.model.dto.PageDateReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 黑名单 分页查询请求
 *
 * @author Codex
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "黑名单分页查询请求")
public class BlacklistPageReq extends PageDateReq {

    @ApiModelProperty(value = "用户ID", example = "1000000000000000001")
    private Long userId;

    @ApiModelProperty(value = "用户姓名", example = "张三")
    private String userName;

    @ApiModelProperty(value = "封禁状态：0-解封，1-封禁", example = "1")
    private String status;

    /**
     * 处理查询条件
     */
    public void handleData() {
    }
}