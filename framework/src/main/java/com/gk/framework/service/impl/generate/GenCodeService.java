package com.gk.framework.service.impl.generate;

import cn.hutool.core.util.CharsetUtil;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.framework.mapper.generate.GenCodeMapper;
import com.gk.framework.model.dto.generate.GenColumnResp;
import com.gk.framework.model.dto.generate.GenReq;
import com.gk.framework.model.dto.generate.GenTableReq;
import com.gk.framework.model.dto.generate.GenTableResp;
import com.gk.framework.service.intf.generate.IGenCodeService;
import com.gk.framework.utils.GenUtils;
import com.gk.framework.utils.VelocityInitializer;
import com.gk.framework.utils.VelocityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 前端代码生成 业务实现类
 *
 * @author GuoYu
 * @since 2024-03-15 16:51
 **/
@Service
@Slf4j
public class GenCodeService implements IGenCodeService {

    @Resource
    private GenCodeMapper genCodeMapper;

    /**
     * 查询所有数据表
     */
    @Override
    public List<GenTableResp> listTable(GenTableReq req) {
        return genCodeMapper.listTable(req);
    }

    /**
     * 查询对应表的所有字段
     */
    @Override
    public List<GenColumnResp> listTableColumn(String tableName) {
        List<GenColumnResp> columnRespList = genCodeMapper.listTableColumn(tableName);
        columnRespList.forEach(GenUtils::handleHtml);
        return columnRespList;
    }

    /**
     * 生成前端代码 zip压缩后下载
     */
    @Override
    public void generatorCode(GenReq req, ZipOutputStream zipOutputStream) {
        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(req);
        List<String> templatePathList = VelocityUtils.getTemplatePathList();
        for (String templatePath : templatePathList) {
            // 渲染模板
            StringWriter stringWriter = new StringWriter();
            Template template = Velocity.getTemplate(templatePath, CharsetUtil.UTF_8);
            template.merge(context, stringWriter);
            try {
                // 添加到zip
                zipOutputStream.putNextEntry(new ZipEntry(VelocityUtils.getFileName(templatePath, context)));
                IOUtils.write(stringWriter.toString(), zipOutputStream, CharsetUtil.UTF_8);
                IOUtils.closeQuietly(stringWriter);
                zipOutputStream.flush();
                zipOutputStream.closeEntry();
            } catch (IOException e) {
                log.error("Generate Code Error: generate front code zip file failed", e);
                throw new SysException(SysStatus.FRONT_CODE_GEN_ERROR);
            }
        }
    }
}
