package com.gk.framework.model.entity.system;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseCreateEntity;
import com.gk.common.utils.JsonUtils;
import com.gk.framework.model.dto.system.sysOperateLog.SysOperateLogResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 操作日志表 数据库模型
 *
 * @author Flame
 * @since 2022-12-19 14:43:46
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysOperateLog extends BaseCreateEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 部门表id
     */
    private Long deptId;

    /**
     * 操作标题
     */
    private String title;

    /**
     * 操作Url
     */
    private String requestUrl;

    /**
     * 操作方式
     */
    private String requestMethod;

    /**
     * 操作类型
     */
    private String type;

    /**
     * 操作ip
     */
    private String ip;

    /**
     * 操作参数
     */
    private String param;

    /**
     * 操作结果
     */
    private String result;

    /**
     * 操作状态
     */
    private String status;

    public SysOperateLog setParam(String param) {
        if (JSONUtil.isTypeJSON(param)) {
            this.param = param;
        } else {
            this.param = JsonUtils.toJson(param);
        }
        return this;
    }

    public SysOperateLog setResult(String result) {
        this.result = StrUtil.sub(result, 0, 4000);
        return this;
    }

    public SysOperateLogResp toResp() {
        return new SysOperateLogResp()
                .setTitle(title)
                .setRequestUrl(requestUrl)
                .setRequestMethod(requestMethod)
                .setType(type)
                .setIp(ip)
                .setParam(param)
                .setResult(result)
                .setStatus(status)
                .setCreateTime(getCreateTime());
    }

}
