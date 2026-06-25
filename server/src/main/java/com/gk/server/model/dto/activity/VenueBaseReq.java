package com.gk.server.model.dto.activity;

import com.gk.framework.validate.InEnum;
import com.gk.server.enums.VenueType;
import com.gk.server.model.entity.activity.Venue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * 场地 基础请求类
 *
 * @author Codex
 */
@Data
public class VenueBaseReq {

    @NotBlank(message = "场地名称不能为空")
    @Length(max = 100, message = "场地名称长度不能超过100")
    @ApiModelProperty(value = "场地名称", required = true, example = "A栋会议室")
    private String name;

    @NotBlank(message = "场地类型不能为空")
    @InEnum(VenueType.class)
    @ApiModelProperty(value = "场地类型：venue-场地，course-课程，activity-活动", required = true, example = "venue")
    private String venueType;

    @ApiModelProperty(value = "场地描述")
    private String description;

    @ApiModelProperty(value = "位置/地址", example = "A栋3楼")
    private String location;

    @Min(value = 1, message = "最大容纳人数不能小于1")
    @ApiModelProperty(value = "最大容纳人数", required = true, example = "50")
    private int maxCapacity;

    @ApiModelProperty(value = "设备配置（JSON）", example = "{\"projector\":true,\"wifi\":true}")
    private String equipment;

    @ApiModelProperty(value = "联系人和联系方式", example = "张三 13800138000")
    private String contact;

    @ApiModelProperty(value = "是否需要审批：0-否，1-是", example = "0")
    private String requireApproval = "0";

    @ApiModelProperty(value = "预约规则（JSON）", example = "{\"maxDays\":7,\"minHours\":1}")
    private String reserveRule;

    @ApiModelProperty(value = "场地图片（JSON数组）", example = "[\"https://xxx.com/img1.jpg\"]")
    private String images;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序号", example = "1")
    private int orderNum = 1;

    /**
     * 转换成数据库操作类
     */
    protected Venue toEntity() {
        return new Venue()
                .setName(name)
                .setVenueType(venueType)
                .setDescription(description)
                .setLocation(location)
                .setMaxCapacity(maxCapacity)
                .setEquipment(equipment)
                .setContact(contact)
                .setRequireApproval(requireApproval)
                .setReserveRule(reserveRule)
                .setImages(images)
                .setRemark(remark)
                .setOrderNum(orderNum);
    }
}