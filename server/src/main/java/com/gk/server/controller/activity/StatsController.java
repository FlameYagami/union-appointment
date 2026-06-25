package com.gk.server.controller.activity;

import com.gk.common.model.others.ApiResult;
import com.gk.server.model.dto.activity.ActivityStatsResp;
import com.gk.server.service.intf.activity.IStatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/activity/stats")
@Api(tags = "活动预约统计")
public class StatsController {

    @Resource
    private IStatsService statsService;

    @GetMapping
    @PreAuthorize("@ss.hasPermission('server:activity-stats:page')")
    @ApiOperation(value = "获取活动预约统计数据", notes = "server:activity-stats:page")
    public ApiResult<ActivityStatsResp> getStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ApiResult.ok(statsService.getActivityStats(startDate, endDate));
    }
}
