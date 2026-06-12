package com.gk.server.model.entity.demo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import com.gk.server.model.dto.demo.serverDemo.ServerDemoResp;
import java.util.Date;
import com.gk.common.constant.DateConstant;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 示例表 数据库模型
 *
 * @author GuoYu
 * @since 2023-04-26 16:06:07
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ServerDemo extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 示例名称
     */
    private String demoName;

    /**
     * 示例编码
     */
    private String demoCode;

    /**
     * 示例时间
     */
    private Date demoTime;

    /**
     * 数据状态
     */
    private String dataStatus;

    /**
     * 示例备注
     */
    private String remark;


    public ServerDemoResp toResp() {
        return new ServerDemoResp()
                .setId(id)
                .setDemoName(demoName)
                .setDemoCode(demoCode)
                .setDemoTime(demoTime)
                .setDataStatus(dataStatus)
                .setRemark(remark);
    }

}
