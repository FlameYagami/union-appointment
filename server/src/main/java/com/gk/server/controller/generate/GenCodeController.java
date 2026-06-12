package com.gk.server.controller.generate;

import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.model.others.ApiResult;
import com.gk.common.utils.StreamHeaderUtils;
import com.gk.framework.annotation.RepeatSubmit;
import com.gk.framework.model.dto.generate.GenColumnResp;
import com.gk.framework.model.dto.generate.GenReq;
import com.gk.framework.model.dto.generate.GenTableReq;
import com.gk.framework.model.dto.generate.GenTableResp;
import com.gk.framework.service.intf.generate.IGenCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * 前端代码生成控制类
 *
 * @author GuoYu
 * @since 2024-03-23 15:35
 **/

@RestController
@RequestMapping("/api/gen-code")
@Api(tags = "前端代码生成")
@Validated
@Slf4j
public class GenCodeController {

    @Resource
    private IGenCodeService genCodeService;

    @GetMapping("/list-table")
    @PreAuthorize("@ss.hasPermission('server:gen-code:list-table')")
    @ApiOperation(value = "查询所有数据表", notes = "server:gen-code:list-table")
    public ApiResult<List<GenTableResp>> pageList(GenTableReq req) {
        return ApiResult.ok(genCodeService.listTable(req));
    }

    @GetMapping("/list-column")
    @PreAuthorize("@ss.hasPermission('server:gen-code:list-column')")
    @ApiOperation(value = "查询对应表的所有字段", notes = "server:gen-code:list-column")
    public ApiResult<List<GenColumnResp>> pageList(@RequestParam @NotBlank(message = "表名缺失") String tableName) {
        return ApiResult.ok(genCodeService.listTableColumn(tableName));
    }

//    @SneakyThrows
    @PostMapping("/download")
    @PreAuthorize("@ss.hasPermission('server:gen-code:download')")
    @RepeatSubmit
    @ApiOperation(value = "前端代码生成Zip下载", notes = "server:gen-code:download")
    public void generateFrontCodeDownloadZip(@RequestBody @Valid GenReq req, HttpServletResponse response) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        genCodeService.generatorCode(req, zipOutputStream);
        IOUtils.closeQuietly(zipOutputStream);
        try {
            byte[] data = outputStream.toByteArray();
            StreamHeaderUtils.setupZipHeader(response, data);
            IOUtils.write(data, response.getOutputStream());
            IOUtils.closeQuietly(outputStream);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            log.error("Generate Code Error: generate front code download zip failed", e);
            throw new SysException(SysStatus.FRONT_CODE_DOWNLOAD_ERROR);
        }
    }
}
