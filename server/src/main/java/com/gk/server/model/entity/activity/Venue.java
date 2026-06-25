package com.gk.server.model.entity.activity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gk.common.model.base.BaseEntity;
import com.gk.server.model.dto.activity.VenueResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 鍦哄湴/璧勬簮瀹炰綋
 *
 * @author Codex
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("act_venue")
public class Venue extends BaseEntity {

    @TableId
    private Long id;
    private String name;
    private String venueType;
    private String description;
    private String location;
    private Integer maxCapacity;
    private String equipment;
    private String contact;
    private String requireApproval;
    private String reserveRule;
    private String images;
    private String remark;
    private Integer orderNum;

    public VenueResp toResp() {
        return new VenueResp()
                .setId(id)
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
