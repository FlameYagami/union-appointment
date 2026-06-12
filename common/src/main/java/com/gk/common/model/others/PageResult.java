package com.gk.common.model.others;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gk.common.intf.IResultStatus;
import com.gk.common.enums.SysStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 接口分页结果封装类
 *
 * @author GuoYu
 * @since 2022-12-08 11:34
 **/
@ApiModel(description = "接口分页结果")
@Data
public class PageResult<T> extends BaseResult {

    /**
     * 总量
     */
    @ApiModelProperty(value = "总量", example = "1")
    private int total;

    /**
     * 数据
     */
    private List<T> data;

    public PageResult(List<T> data, int total, IResultStatus status) {
        super(status);
        this.data = data;
        this.total = total;
    }

    /**
     * 业务成功
     */
    public static <T> PageResult<T> ok(List<T> data) {
        return new PageResult<>(data, data.size(), SysStatus.SUCCESS);
    }

    /**
     * 业务成功
     */
    public static <T> PageResult<T> ok(IPage<T> page) {
        return new PageResult<>(page.getRecords(), (int)page.getTotal(), SysStatus.SUCCESS);
    }

}
