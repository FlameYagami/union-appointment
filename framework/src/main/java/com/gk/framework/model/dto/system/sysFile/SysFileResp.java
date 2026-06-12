package com.gk.framework.model.dto.system.sysFile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 文件信息 响应类
 *
 * @author Flame
 * @since 2023-02-27 17:21:35
 */
@ApiModel("文件信息")
@Data
@Accessors(chain = true)
public class SysFileResp {

    @ApiModelProperty(value = "文件id", example = "1000000000000000001")
    private long id;

    @ApiModelProperty(value = "文件名", example = "5ea69f99-ae68-49ce-b144-8837d636b733.jpg")
    private String filename;

    @ApiModelProperty(value = "文件大小", example = "10MB")
    private String fileSize;

    @ApiModelProperty(value = "https://domain.com/files/biz/20220101/5ea69f99-ae68-49ce-b144-8837d636b733.jpg")
    private String url;


}
