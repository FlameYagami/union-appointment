package com.gk.server.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.BlacklistPageReq;
import com.gk.server.model.entity.activity.AppointmentBlacklist;
import org.apache.ibatis.annotations.Param;

/**
 * 黑名单 Mapper
 *
 * @author Codex
 */
public interface AppointmentBlacklistMapper extends BaseMapper<AppointmentBlacklist> {

    IPage<AppointmentBlacklist> pageBlacklist(IPage<AppointmentBlacklist> page, @Param("req") BlacklistPageReq req);

    boolean selectExists(@Param("userId") long userId);
}