package com.gk.server.model.dto.activity;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@Accessors(chain = true)
public class ActivityStatsResp {

    /** 总预约量 */
    private Long totalAppointments;

    /** 总预约人数 */
    private Long totalPersons;

    /** 通过率 */
    private String approvalRate;

    /** 违约率 */
    private String violationRate;

    /** 各场地预约量 */
    private List<VenueStats> venueStats;

    /** 各月份预约趋势 */
    private List<MonthStats> monthStats;

    /** 各状态预约分布 */
    private List<StatusStats> statusStats;

    /** 职工参与排行 */
    private List<DeptStats> deptStats;

    /** 各时段预约分布 */
    private List<TimeSlotStats> timeSlotStats;

    @Data
    @Accessors(chain = true)
    public static class VenueStats {
        private Long venueId;
        private String venueName;
        private String venueType;
        private Long appointmentCount;
        private Long personCount;
        private String usageRate;
        private Long maxCapacity;
    }

    @Data
    @Accessors(chain = true)
    public static class MonthStats {
        private String month;
        private Long count;
        private Long approvedCount;
        private Long cancelledCount;
    }

    @Data
    @Accessors(chain = true)
    public static class StatusStats {
        private String status;
        private String statusName;
        private Long count;
    }

    @Data
    @Accessors(chain = true)
    public static class DeptStats {
        private Long deptId;
        private String deptName;
        private Long count;
        private Long personCount;
    }

    @Data
    @Accessors(chain = true)
    public static class TimeSlotStats {
        private String timeSlot;
        private Long count;
    }
}
