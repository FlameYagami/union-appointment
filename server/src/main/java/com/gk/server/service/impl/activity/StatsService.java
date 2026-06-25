package com.gk.server.service.impl.activity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.gk.server.mapper.activity.StatsMapper;
import com.gk.server.model.dto.activity.ActivityStatsResp;
import com.gk.server.service.intf.activity.IStatsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService implements IStatsService {

    @Resource
    private StatsMapper statsMapper;

    @Override
    public ActivityStatsResp getActivityStats(String startDate, String endDate) {
        if (StrUtil.isBlank(startDate)) {
            startDate = LocalDate.now().withDayOfYear(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        if (StrUtil.isBlank(endDate)) {
            endDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        ActivityStatsResp resp = new ActivityStatsResp();

        // 1. How many total appointments
        Long total = statsMapper.countTotal(startDate, endDate);
        resp.setTotalAppointments(total != null ? total : 0L);

        // 2. How many persons
        Long persons = statsMapper.countTotalPersons(startDate, endDate);
        resp.setTotalPersons(persons != null ? persons : 0L);

        // 3. approval rate & violation rate
        Long approved = statsMapper.countByStatus(startDate, endDate, "approved");
        Long violated = statsMapper.countByStatus(startDate, endDate, "violated");
        Long totalForRate = statsMapper.countCompleted(startDate, endDate);
        if (totalForRate != null && totalForRate > 0) {
            resp.setApprovalRate(percentage(approved, totalForRate));
            resp.setViolationRate(percentage(violated, totalForRate));
        } else {
            resp.setApprovalRate("0%");
            resp.setViolationRate("0%");
        }

        // 4. venue stats
        resp.setVenueStats(statsMapper.venueStats(startDate, endDate));

        // 5. month trend
        resp.setMonthStats(buildMonthStats(startDate, endDate));

        // 6. status distribution
        resp.setStatusStats(statsMapper.statusStats(startDate, endDate));

        // 7. dept ranking
        resp.setDeptStats(statsMapper.deptStats(startDate, endDate));

        // 8. timeSlot distribution
        resp.setTimeSlotStats(statsMapper.timeSlotStats(startDate, endDate));

        return resp;
    }

    private List<ActivityStatsResp.MonthStats> buildMonthStats(String startDate, String endDate) {
        List<ActivityStatsResp.MonthStats> dbList = statsMapper.monthStats(startDate, endDate);
        Map<String, ActivityStatsResp.MonthStats> map = new LinkedHashMap<>();

        // generate all months in range
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate cursor = start.withDayOfMonth(1);
        while (!cursor.isAfter(end)) {
            String key = cursor.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            map.put(key, new ActivityStatsResp.MonthStats()
                    .setMonth(key).setCount(0L).setApprovedCount(0L).setCancelledCount(0L));
            cursor = cursor.plusMonths(1);
        }

        // fill data
        for (ActivityStatsResp.MonthStats s : dbList) {
            map.put(s.getMonth(), s);
        }

        return new ArrayList<>(map.values());
    }

    private String percentage(Long part, Long total) {
        if (total == null || total == 0) return "0%";
        return new BigDecimal(part).multiply(new BigDecimal(100))
                .divide(new BigDecimal(total), 1, RoundingMode.HALF_UP) + "%";
    }
}
