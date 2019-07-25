package com.ngsky.ice.rest.service;

import lombok.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: sunyx@lenovocloud.com
 * @CreateDate: 2019/7/25 13:31
 */
public interface FileDownloadService {
    /**
     * 下载文件
     */
    void download(@NonNull String fileHash, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
