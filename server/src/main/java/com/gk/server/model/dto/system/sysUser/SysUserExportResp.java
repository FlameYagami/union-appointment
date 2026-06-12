package com.gk.server.model.dto.system.sysUser;

import cn.hutool.core.util.DesensitizedUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.gk.framework.annotation.DictFormat;
import com.gk.framework.constant.DictConstant;
import com.gk.framework.convert.DictConvert;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户 导出响应类
 *
 * @author Flame
 * @since 2022-12-28 17:22:56
 */
@ApiModel(value = "用户导出响应")
@Data
@Accessors(chain = true)
@HeadRowHeight(20)
public class SysUserExportResp {

    @ExcelProperty("账号")
    private String username;

    @ExcelProperty("姓名")
    private String nickname;

    @ExcelProperty("所属部门")
    private String deptNames;

    @ExcelProperty("性别")
    private String gender;

    @ExcelProperty("手机号")
    private String telephone;

    @ExcelProperty(value = "账号状态", converter = DictConvert.class)
    @DictFormat(DictConstant.CODE_DATA_STATUS)
    private String dataStatus;

    public void lateInit() {
        telephone = DesensitizedUtil.mobilePhone(telephone);
    }

}
