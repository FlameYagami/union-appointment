package com.gk.framework.service.impl.system;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gk.common.constant.CommonConstant;
import com.gk.common.constant.DateConstant;
import com.gk.common.enums.DataStatus;
import com.gk.common.enums.EnabledType;
import com.gk.common.enums.SysStatus;
import com.gk.common.model.exception.SysException;
import com.gk.common.utils.JsonUtils;
import com.gk.common.utils.StringExtUtils;
import com.gk.framework.mapper.system.SysFileMapper;
import com.gk.framework.model.dto.system.sysFile.SysFileResp;
import com.gk.framework.model.entity.system.SysFile;
import com.gk.framework.service.intf.system.ISysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * slash
 * 文件表 服务实现类
 *
 * @author Flame
 * @since 2023-02-27 17:21:35
 */
@Service
@Slf4j
public class SysFileService extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Value("${basePath.file}")
    private String basePath;

    private final static String FILE_DIR = "/files/";

    /**
     * 文件上传到本地(默认不启用文件)
     *
     * @param uploadDir 上传的文件夹名称
     * @param bizType   业务类型
     */
    @Override
    public SysFileResp upload(MultipartFile file, String uploadDir, String bizType) {
        return upload(file, uploadDir, bizType, true, EnabledType.DISABLE);
    }

    /**
     * 文件上传到本地
     *
     * @param uploadDir   上传的文件夹
     * @param bizType     业务类型
     * @param enabledType 文件是否启用
     */
    @Override
    public SysFileResp upload(MultipartFile file, String uploadDir, String bizType, EnabledType enabledType) {
        return upload(file, uploadDir, bizType, true, enabledType);
    }

    /**
     * 文件上传到本地
     *
     * @param uploadDir    上传的文件夹
     * @param bizType      业务类型
     * @param needDatePath 日期路径是否启用
     * @param enabledType  文件是否启用
     */
    @Override
    public SysFileResp upload(MultipartFile file, String uploadDir, String bizType, boolean needDatePath, EnabledType enabledType) {
        String originalFilename = getOriginalFilename(file);
        // 构建上传的文件夹路径
        if (needDatePath) {
            uploadDir = uploadDir + DateUtil.format(new Date(), DateConstant.SIMPLE_DATE_PATTERN) + CommonConstant.SLASH; // avatar/20200203/
        }
        // 获取文件扩展名
        String extName = getExtName(file);
        // 构建磁盘文件名
        String diskFilename = IdUtil.randomUUID() + CommonConstant.DOT + extName; // 267b824d-2663-465c-973d-9036a8bf9871.jpg
        return upload(file, uploadDir, originalFilename, diskFilename, bizType, enabledType);
    }

    /**
     * 文件上传到本地
     *
     * @param uploadDir        上传的文件夹
     * @param originalFilename 原始的文件名
     * @param diskFilename     磁盘文件名
     * @param bizType          业务类型
     * @param enabledType      文件是否启用
     */
    @Override
    public SysFileResp upload(MultipartFile file, String uploadDir, String originalFilename, String diskFilename, String bizType, EnabledType enabledType) {
        // 创建文件保存路径
        String fileSavePath = basePath + uploadDir + diskFilename;
        // 保存源文件
        File saveFile = writeFile(file, fileSavePath);
        // 保存文件信息
        SysFile sysFile = saveFile(saveFile, uploadDir, originalFilename, diskFilename, bizType, enabledType);
        // 文件链接
        return sysFile.toResp(FILE_DIR);
    }

    /**
     * 文件上传到本地(默认不启用文件)
     *
     * @param uploadDir 上传的文件夹名称
     * @param bizType   业务类型
     */
    @Override
    public SysFileResp uploadWithThumb(MultipartFile file, float scale, String uploadDir, String bizType) {
        return uploadWithThumb(file, scale, uploadDir, bizType, EnabledType.DISABLE);
    }

    /**
     * 文件上传到本地
     *
     * @param uploadDir   上传的文件夹
     * @param bizType     业务类型
     * @param enabledType 文件是否启用
     */
    @Override
    public SysFileResp uploadWithThumb(MultipartFile file, float scale, String uploadDir, String bizType, EnabledType enabledType) {
        String originalFilename = getOriginalFilename(file);
        // 构建上传的文件夹路径
        uploadDir = uploadDir + DateUtil.format(new Date(), DateConstant.SIMPLE_DATE_PATTERN) + CommonConstant.SLASH; // avatar/20200203/
        // 获取文件扩展名
        String extName = getExtName(file);
        // 构建磁盘文件名
        String diskFilename = IdUtil.randomUUID() + CommonConstant.DOT + extName; // 267b824d-2663-465c-973d-9036a8bf9871.jpg
        return uploadWithThumb(file, scale, uploadDir, originalFilename, diskFilename, bizType, enabledType);
    }

    /**
     * 文件上传到本地并生成缩略图
     *
     * @param scale            图片缩放比例
     * @param uploadDir        上传的文件夹
     * @param originalFilename 原始的文件名
     * @param diskFilename     磁盘文件名
     * @param bizType          业务类型
     * @param enabledType      文件是否启用
     */
    @Override
    public SysFileResp uploadWithThumb(MultipartFile file, float scale, String uploadDir, String originalFilename, String diskFilename, String bizType, EnabledType enabledType) {
        // 创建文件保存路径
        String fileSavePath = basePath + uploadDir + diskFilename;
        // 保存源文件
        File saveFile = writeFile(file, fileSavePath);
        // 保存缩略文件
        writeThumbFile(saveFile, uploadDir, diskFilename, scale);
        // 保存文件信息
        SysFile sysFile = saveFile(saveFile, uploadDir, originalFilename, diskFilename, bizType, enabledType);
        // 文件链接
        return sysFile.toResp(FILE_DIR);
    }

    /**
     * 获取文件名
     */
    private String getOriginalFilename(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (null == originalFilename) {
            originalFilename = CommonConstant.EMPTY;
        }
        // 对超过32位长度的文件名进行截取
        if (32 <= originalFilename.length()) {
            originalFilename = StrUtil.subWithLength(originalFilename, 0, 32);
        }
        return originalFilename;
    }

    /**
     * 通过路径保存文件
     */
    private File writeFile(MultipartFile file, String fileSavePath) {
        try {
            return FileUtil.writeBytes(file.getBytes(), fileSavePath);
        } catch (Exception e) {
            log.error("Upload file error: ({})", e.getMessage());
            throw new SysException(SysStatus.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 通过路径保存缩率文件
     */
    private void writeThumbFile(File saveFile, String uploadDir, String diskFilename, float scale) {
        String thumbDir = basePath + uploadDir + CommonConstant.THUMB_DIR;
        FileUtil.mkdir(thumbDir);
        String thumbSavePath = thumbDir + diskFilename;
        File thumbFile = FileUtil.file(thumbSavePath);
        ImgUtil.scale(saveFile, thumbFile, scale);
    }

    /**
     * 获取文件扩展名
     */
    public static String getExtName(MultipartFile file) {
        // 从文件名获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String extName = FileNameUtil.extName(originalFilename);
        if (StrUtil.isNotEmpty(extName)) {
            return extName;
        }

        // 如果获取不到, 尝试从contentType获取
        String contentType = file.getContentType();
        if (StrUtil.isNotEmpty(contentType) && contentType.contains(CommonConstant.SLASH)) {
            return contentType.split(CommonConstant.SLASH)[1];
        }

        // 返回未知扩展名
        return "unknown";
    }

    /**
     * 保存文件到数据库
     */
    private SysFile saveFile(File saveFile, String uploadDir, String originalFilename, String diskFilename, String bizType, EnabledType enabledType) {
        // 获取文件大小的描述
        String fileSize = FileUtil.readableFileSize(saveFile);
        SysFile sysFile = new SysFile()
                .setBizType(bizType)
                .setFileSize(fileSize)
                .setFilename(originalFilename)
                .setFilePath(uploadDir + diskFilename)
                .setDeleted(DataStatus.DISABLE.value);
        // 手动设置文件启用值
        sysFile.setEnabled(enabledType.value);

        // 保存文件
        if (!save(sysFile)) {
            log.error("Save SysFile error: {}", JsonUtils.toJson(sysFile));
            throw new SysException(SysStatus.FILE_UPLOAD_ERROR);
        }
        return sysFile;
    }

    /**
     * 修改文件的使用状态
     *
     * @param saveFileIds 保存的文件id集合
     * @param dbFileIds   数据库的文件id集合
     */
    @Override
    public boolean updateEnabled(List<Long> saveFileIds, List<Long> dbFileIds) {
        // 查询需要删除的文件ids
        List<Long> deleteIds = dbFileIds.stream()
                .filter(dbId -> !saveFileIds.contains(dbId))
                .collect(Collectors.toList());
        // 弃用文件
        if (!updateEnabled(deleteIds, EnabledType.DISABLE)) {
            log.error("Disable SysFile error: SysFileIds({}) in delete", JsonUtils.toJson(deleteIds));
            return false;
        }
        // 启用文件
        if (!updateEnabled(saveFileIds, EnabledType.ENABLE)) {
            log.error("Enabled SysFile error: SysFileIds({}) in update", JsonUtils.toJson(saveFileIds));
            return false;
        }
        return true;
    }

    /**
     * 修改文件的使用状态
     */
    @Override
    public boolean updateEnabled(long fileId, EnabledType enabledType) {
        return updateEnabled(List.of(fileId), enabledType);
    }

    /**
     * 修改文件的使用状态
     */
    @Override
    public boolean updateEnabled(List<Long> fileIds, EnabledType enabledType) {
        if (fileIds.isEmpty()) {
            return true;
        }
        return lambdaUpdate()
                .set(SysFile::getEnabled, enabledType.value)
                .in(SysFile::getId, fileIds)
                .update();
    }

    /**
     * 获取文件的链接
     */
    @Override
    public String getFileUrl(long fileId) {
        SysFile sysFile = lambdaQuery()
                .eq(SysFile::getEnabled, EnabledType.ENABLE.value)
                .eq(SysFile::getId, fileId)
                .one();
        if (null == sysFile) {
            log.error("Can not find SysFile: fileId({})", fileId);
            return CommonConstant.EMPTY;
        }
        return FILE_DIR + sysFile.getFilePath();
    }

    /**
     * 获取文件的链接
     */
    @Override
    public List<String> getFileUrls(List<Long> fileIds) {
        if (fileIds.isEmpty()) {
            return new ArrayList<>();
        }
        return lambdaQuery()
                .eq(SysFile::getEnabled, EnabledType.ENABLE.value)
                .in(SysFile::getId, fileIds)
                .list().stream()
                .map(it -> FILE_DIR + it.getFilePath())
                .collect(Collectors.toList());
    }

    /**
     * 获取文件列表详情
     */
    @Override
    public List<SysFileResp> listSysFile(List<Long> fileIds) {
        if (fileIds.isEmpty()) {
            return new ArrayList<>();
        }
        return lambdaQuery()
                .eq(SysFile::getEnabled, EnabledType.ENABLE.value)
                .in(SysFile::getId, fileIds)
                .list().stream()
                .map(it -> it.toResp(FILE_DIR))
                .collect(Collectors.toList());
    }

    /**
     * 获取文件列表详情(查询不到的fileId直接填充空的数据模型)
     */
    @Override
    public List<SysFileResp> listSysFileWithEmpty(List<Long> fileIds) {
        List<SysFileResp> resp = listSysFile(fileIds);
        // 文件id、文件响应分组
        Map<Long, SysFileResp> fileIdRespMap = resp.stream()
                .collect(Collectors.toMap(SysFileResp::getId, it -> it));
        // 依照fileIds的顺序依次构建返回的模型, 如果对应的fileId在数据中不存在, 则创建一个空的模型返回
        return fileIds.stream()
                .map(fileId -> fileIdRespMap.containsKey(fileId) ? fileIdRespMap.get(fileId) : new SysFileResp().setId(fileId))
                .collect(Collectors.toList());
    }

    /**
     * 获取文件列表详情(查询不到的fileId直接填充空的数据模型)
     */
    @Override
    public List<SysFileResp> listSysFileWithEmpty(String fileIds) {
        return listSysFileWithEmpty(StringExtUtils.splitComma(fileIds));
    }

}

