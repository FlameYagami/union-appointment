package com.gk.framework.model.dto.system.sysOpenConfig;

import com.gk.framework.model.entity.system.SysOpenConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.tuple.Triple;

/**
 * 第三方对接配置 新增请求类
 *
 * @author Flame
 * @since 2023-03-13 16:26:48
 */
@ApiModel(value = "第三方对接配置新增请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOpenConfigAddReq extends SysOpenConfigBaseReq {

    public SysOpenConfig toEntity(Triple<String, String, String> aes) {
        return super.toEntity()
                .setOpenId(aes.getLeft())
                .setAesKey(aes.getMiddle())
                .setAesIv(aes.getRight());
    }

}
