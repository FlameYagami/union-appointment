package com.gk.server.service.intf.activity;

import com.gk.server.model.dto.activity.ActivityStatsResp;

public interface IStatsService {

    ActivityStatsResp getActivityStats(String startDate, String endDate);
}
