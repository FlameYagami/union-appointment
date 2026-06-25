package com.gk.server.model.dto.activity;

import com.gk.common.enums.EnabledType;
import com.gk.server.enums.VenueType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 场地 响应类
 *
 * @author Codex
 */
@ApiModel(value = "场地响应")
@Data
@Accessors(chain = true)
public class VenueResp {

    @ApiModelProperty(value = "主键", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "场地名称", example = "A栋会议室")
    private String name;

    @ApiModelProperty(value = "场地类型：venue-场地，course-课程，activity-活动", example = "venue")
    private String venueType;

    @ApiModelProperty(value = "场地类型名称", example = "场地")
    private String venueTypeName;

    @ApiModelProperty(value = "场地描述")
    private String description;

    @ApiModelProperty(value = "位置/地址", example = "A栋3楼")
    private String location;

    @ApiModelProperty(value = "最大容纳人数", example = "50")
    private int maxCapacity;

    @ApiModelProperty(value = "设备配置（JSON）", example = "{\"projector\":true,\"wifi\":true}")
    private String equipment;

    @ApiModelProperty(value = "联系人和联系方式", example = "张三 13800138000")
    private String contact;

    @ApiModelProperty(value = "是否需要审批：0-否，1-是", example = "0")
    private String requireApproval;

    @ApiModelProperty(value = "是否需要审批名称", example = "否")
    private String requireApprovalName;

    @ApiModelProperty(value = "预约规则（JSON）", example = "{\"maxDays\":7,\"minHours\":1}")
    private String reserveRule;

    @ApiModelProperty(value = "场地图片（JSON数组）", example = "[\"https://xxx.com/img1.jpg\"]")
    private String images;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序号", example = "1")
    private int orderNum;

    @ApiModelProperty(value = "可用状态：0-禁用，1-启用", example = "1")
    private String enabled;

    @ApiModelProperty(value = "创建时间", example = "2026-01-01 10:00:00")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2026-01-01 10:00:00")
    private Date updateTime;

    /**
     * 处理数据
     */
    public void handleData() {
        VenueType venueTypeEnum = VenueType.getInstance(venueType);
        this.venueTypeName = venueTypeEnum != null ? venueTypeEnum.desc : venueType;
        this.requireApprovalName = EnabledType.ENABLE.value.equals(requireApproval) ? "是" : "否";
    }
}
