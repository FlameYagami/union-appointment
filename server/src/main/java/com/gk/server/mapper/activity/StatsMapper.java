package com.gk.server.mapper.activity;

import com.gk.server.model.dto.activity.ActivityStatsResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatsMapper {

    Long countTotal(@Param("startDate") String startDate, @Param("endDate") String endDate);

    Long countTotalPersons(@Param("startDate") String startDate, @Param("endDate") String endDate);

    Long countByStatus(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("status") String status);

    Long countCompleted(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ActivityStatsResp.VenueStats> venueStats(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ActivityStatsResp.MonthStats> monthStats(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ActivityStatsResp.StatusStats> statusStats(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ActivityStatsResp.DeptStats> deptStats(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<ActivityStatsResp.TimeSlotStats> timeSlotStats(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
