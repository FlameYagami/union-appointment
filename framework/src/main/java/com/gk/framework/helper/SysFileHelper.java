package com.gk.framework.helper;

import cn.hutool.extra.spring.SpringUtil;
import com.gk.common.enums.EnabledType;
import com.gk.common.utils.StringExtUtils;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import com.gk.framework.service.intf.system.ISysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 系统文件辅助类
 *
 * @author Flame
 * @date 2023-03-20 11:44
 **/
@Slf4j
public class SysFileHelper {

    private final ISysFileService sysFileService;

    public SysFileHelper() {
        sysFileService = SpringUtil.getBean(ISysFileService.class);
    }

    public static SysFileHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final SysFileHelper INSTANCE = new SysFileHelper();
    }

    /**
     * 根据文件id获取文件Url链接
     */
    public String getFileUrl(long fileId) {
        return sysFileService.getFileUrl(fileId);
    }

    /**
     * 根据文件ids获取文件列表详情
     */
    public List<SysFileResp> listSysFile(String fileIds) {
        List<Long> ids = StringExtUtils.splitComma(fileIds);
        return sysFileService.listSysFile(ids);
    }

    /**
     * 文件上传(默认不启用文件)
     *
     * @param uploadDir 上传的文件夹
     * @param bizType   业务类型
     */
    public SysFileResp upload(MultipartFile file, String uploadDir, String bizType) {
        return sysFileService.upload(file, uploadDir, bizType);
    }

    /**
     * 文件上传
     *
     * @param uploadDir   上传的文件夹
     * @param bizType     业务类型
     * @param enabledType 文件是否启用
     */
    public SysFileResp upload(MultipartFile file, String uploadDir, String bizType, EnabledType enabledType) {
        return sysFileService.upload(file, uploadDir, bizType, enabledType);
    }

    /**
     * 文件上传
     *
     * @param uploadDir   上传的文件夹
     * @param bizType     业务类型
     * @param enabledType 文件是否启用
     */
    public SysFileResp upload(MultipartFile file, String uploadDir, String bizType, boolean needDatePath, EnabledType enabledType) {
        return sysFileService.upload(file, uploadDir, bizType, needDatePath, enabledType);
    }

    /**
     * 文件上传(默认不启用文件)
     *
     * @param uploadDir 上传的文件夹
     * @param bizType   业务类型
     */
    public SysFileResp uploadWithThumb(MultipartFile file, String uploadDir, String bizType) {
        return sysFileService.uploadWithThumb(file, 0.1f, uploadDir, bizType);
    }

    /**
     * 修改单文件的使用状态
     */
    public boolean updateEnabled(long fileId, EnabledType enabledType) {
        return sysFileService.updateEnabled(fileId, enabledType);
    }

    /**
     * 修改多文件的使用状态
     */
    public boolean updateBatchEnabled(List<Long> fileIds, EnabledType enabledType) {
        return sysFileService.updateEnabled(fileIds, enabledType);
    }

    /**
     * 调整附件列表时, 修改多文件的使用状态
     */
    public boolean updateBatchCompare(List<Long> saveFileIds, List<Long> dbFileIds) {
        return sysFileService.updateEnabled(saveFileIds, dbFileIds);
    }
}
