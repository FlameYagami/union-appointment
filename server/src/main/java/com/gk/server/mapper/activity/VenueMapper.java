package com.gk.server.mapper.activity;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.server.model.dto.activity.VenuePageReq;
import com.gk.server.model.entity.activity.Venue;
import org.apache.ibatis.annotations.Param;

/**
 * 场地 Mapper
 *
 * @author Codex
 */
public interface VenueMapper extends BaseMapper<Venue> {

    IPage<Venue> pageVenue(IPage<Venue> page, @Param("req") VenuePageReq req);
}