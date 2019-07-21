package com.ngsky.ice.rest.service;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <dl>
 * <dt>FileUploadService</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/12/2019 12:35 AM</dd>
 * </dl>
 *
 * @author ngsky
 */
public interface FileUploadService {

    /**
     * 上传文件
     */
    void upload(@NonNull MultipartFile file) throws Exception;

    /**
     * 下载文件
     */
    void download(@NonNull String fileHash, HttpServletResponse response) throws Exception;
}
