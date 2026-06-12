package com.gk.framework.model.dto.system.sysNotice;

import com.gk.framework.model.entity.system.SysNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * 公告通知 修改请求类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@ApiModel(value = "公告通知修改请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticeEditReq extends SysNoticeBaseReq {

    @ApiModelProperty(value = "主键", required = true, example = "1000000000000000001")
    @Min(value = 1000000000000000001L, message = "id不合法")
    private long id;

    @Override
    public SysNotice toEntity() {
        return super.toEntity()
                .setId(id);
    }

}
