package com.gk.framework.model.dto.system.sysBlacklist;

import cn.hutool.core.util.DesensitizedUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 黑名单 导出响应类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "黑名单导出响应")
@Data
@Accessors(chain = true)
@HeadRowHeight(20)
public class SysBlacklistExportResp {

    @ExcelProperty("账号")
    private String username;

    @ExcelProperty("姓名")
    private String nickname;

    @ExcelProperty("手机号")
    private String telephone;

    public void handleData() {
        telephone = DesensitizedUtil.mobilePhone(telephone);
    }

}
