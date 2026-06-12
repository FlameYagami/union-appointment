package com.gk.framework.model.dto.system.sysNotice;

import com.gk.framework.model.entity.system.SysNotice;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公告通知 新增请求类
 *
 * @author Flame
 * @since 2023-03-08 10:26:30
 */
@ApiModel(value = "公告通知新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticeAddReq extends SysNoticeBaseReq {

    @Override
    public SysNotice toEntity() {
        return super.toEntity();
    }

}
