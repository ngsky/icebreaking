package com.ngsky.ice.rest.api;

import com.ngsky.ice.rest.pojo.Message;
import com.ngsky.ice.rest.service.FileDownloadService;
import com.ngsky.ice.rest.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <dl>
 * <dt>AgentController</dt>
 * <dd>Description:</dd>
 * <dd>CreateDate: 7/6/2019 9:09 PM</dd>
 * </dl>
 *
 * @author ngsky
 */
@Slf4j
@RestController
@RequestMapping("/rest")
public class AgentController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileDownloadService fileDownloadService;

    @PutMapping("/files")
    public Message uploadFiles(@RequestParam(name = "files") MultipartFile[] files) {
        log.info("--- upload file ---");
        for (MultipartFile mf : files) {
            try {
                fileUploadService.upload(mf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Message().ok();
    }

    @GetMapping("/file/{fileHash}")
    public void downloadFile(@PathVariable(name = "fileHash", required = false) String fileHash,
                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileDownloadService.download(fileHash, request, response);
    }
}
