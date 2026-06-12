package com.gk.framework.service.intf.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gk.common.enums.EnabledType;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import com.gk.framework.model.entity.system.SysFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件表 服务接口类
 *
 * @author Flame
 * @since 2023-02-27 17:21:35
 */
public interface ISysFileService extends IService<SysFile> {

    SysFileResp upload(MultipartFile file, String uploadDir, String fileBizType);

    SysFileResp upload(MultipartFile file, String uploadDir, String fileBizType, EnabledType enabledType);

    SysFileResp upload(MultipartFile file, String uploadDir, String fileBizType, boolean needDatePath, EnabledType enabledType);

    SysFileResp upload(MultipartFile file, String uploadDir, String originalFilename, String diskFilename, String fileBizType, EnabledType enabledType);

    SysFileResp uploadWithThumb(MultipartFile file, float scale, String uploadDir, String bizType);

    SysFileResp uploadWithThumb(MultipartFile file, float scale, String uploadDir, String bizType, EnabledType enabledType);

    SysFileResp uploadWithThumb(MultipartFile file, float scale, String uploadDir, String originalFilename, String diskFilename, String bizType, EnabledType enabledType);

    boolean updateEnabled(List<Long> saveFileIds, List<Long> dbFileIds);

    boolean updateEnabled(long fileId, EnabledType enabledType);

    boolean updateEnabled(List<Long> fileIds, EnabledType enabledType);

    String getFileUrl(long fileId);

    List<String> getFileUrls(List<Long> fileIds);

    List<SysFileResp> listSysFile(List<Long> fileIds);

    List<SysFileResp> listSysFileWithEmpty(List<Long> fileIds);

    List<SysFileResp> listSysFileWithEmpty(String fileIds);
}
