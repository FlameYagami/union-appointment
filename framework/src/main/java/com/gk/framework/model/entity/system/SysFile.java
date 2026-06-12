package com.gk.framework.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.gk.common.model.base.BaseEntity;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文件表 数据库模型
 *
 * @author Flame
 * @since 2023-02-27 17:21:35
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SysFile extends BaseEntity {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 业务类型(1:用户头像)
     */
    private String bizType;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 是否删除(1:是 0:否; 默认:0)
     */
    private String deleted;

    public SysFileResp toResp(String fileDir) {
        return new SysFileResp()
                .setId(id)
                .setFilename(filename)
                .setFileSize(fileSize)
                .setUrl(fileDir + filePath);
    }

}
