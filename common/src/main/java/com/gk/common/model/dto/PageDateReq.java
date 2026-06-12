package com.gk.common.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gk.common.constant.CommonConstant;
import com.gk.common.constant.DateConstant;
import com.gk.common.enums.PageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 分页请求封装
 *
 * @author Flame
 * @since 2020/04/20
 **/
@ApiModel(description = "分页请求")
@Data
public class PageDateReq extends DataScopeReq {

    @ApiModelProperty(value = "分页类型(1:全部, 2:总数, 3:数据)[不传参数为全部查询]", example = "1")
    private String pageType;

    @ApiModelProperty(value = "分页大小", example = "15", required = true)
    private int pageSize;

    @ApiModelProperty(value = "分页页数", example = "1", required = true)
    private int pageNum;

    @ApiModelProperty(value = "开始日期", example = "2022-01-01 00:00:00")
    @DateTimeFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private String startTime;

    @ApiModelProperty(value = "结束日期", example = "2022-01-01 23:59:59")
    @DateTimeFormat(pattern = DateConstant.DEFAULT_PATTERN)
    private String endTime;

    /**
     * 通用分页构造
     */
    public <T> Page<T> createPage() {
        return autoCreatePage();
    }

    /**
     * 处理数据
     */
    private <T> Page<T> autoCreatePage() {
        Page<T> page;
        if (PageType.TOTAL.value.equals(pageType)) {
            page = new Page<>(1, 0);
        } else if (PageType.DATA.value.equals(pageType)) {
            page = new Page<>(pageNum, pageSize, false);
        } else {
            pageSize = Math.min(pageSize, CommonConstant.PAGE_SIZE_LIMIT);
            pageSize = pageSize == 0 ? CommonConstant.DEFAULT_PAGE_SIZE : pageSize;
            page = new Page<>(pageNum, pageSize);
        }
        page.setOptimizeCountSql(false);
        return page;
    }

}
