package com.gk.framework.model.dto.system.sysConfig;

import cn.hutool.json.JSONUtil;
import com.gk.common.enums.DataStatus;
import com.gk.framework.model.entity.system.SysConfig;
import com.gk.framework.validate.InEnum;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 配置 基础请求类
 *
 * @author GuoYu
 * @since 2023-03-06 14:56:24
 */
@Data
public class SysConfigBaseReq {

    @ApiModelProperty(value = "配置名称[32]", required = true, example = "配置名称")
    @NotBlank(message = "配置名称不能为空")
    @Length(max = 32, message = "配置名称最多输入32个字符")
    private String configName;

    @ApiModelProperty(value = "配置键[32]", required = true, example = "配置键")
    @NotBlank(message = "配置键不能为空")
    @Length(max = 32, message = "配置键最多输入32个字符")
    private String configKey;

    @ApiModelProperty(value = "配置值[500]", required = true, example = "配置值")
    @NotBlank(message = "配置值不能为空")
    @Length(max = 500, message = "配置值最多输入500个字符")
    private String configValue;

    @ApiModelProperty(value = "配置状态(1:正常, 0:停用)", required = true, example = "1")
    @NotBlank(message = "配置状态不能为空")
    @InEnum(DataStatus.class)
    private String dataStatus;

    @ApiModelProperty(value = "配置备注[255]", example = "配置备注")
    @Length(max = 255, message = "配置备注最多输入255个字符")
    private String remark;

    /**
    * 转换成数据库操作类
    */
    protected SysConfig toEntity() {
        // 对Json类型的值进行格式化(去掉多余空格和换行)
        if (JSONUtil.isTypeJSON(configValue)) {
            JsonElement element = JsonParser.parseString(configValue);
            configValue = new Gson().toJson(element);
        }
        return new SysConfig()
                .setConfigName(configName)
                .setConfigKey(configKey)
                .setConfigValue(configValue)
                .setDataStatus(dataStatus)
                .setRemark(remark);
    }

}
