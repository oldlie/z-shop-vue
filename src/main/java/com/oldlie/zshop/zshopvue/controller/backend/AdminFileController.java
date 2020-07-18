package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.exception.AppRestException;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.UploadFile;
import com.oldlie.zshop.zshopvue.model.response.wangeditor.ImageResponse;
import com.oldlie.zshop.zshopvue.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RequestMapping("/backend/file")
@RestController
public class AdminFileController {

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    // region upload

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageResponse uploadFile(MultipartHttpServletRequest request,
                                    @SessionAttribute("username") String username)
            throws AppRestException {
        ImageResponse response = new ImageResponse();

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;

        Calendar calendar = Calendar.getInstance();
        List<String> uploadFileList = new ArrayList<>();

        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());

            String ofn = mpf.getOriginalFilename();
            if (ofn == null) {
                throw new AppRestException("Upload file name is null", HTTP_CODE.EXCEPTION);
            }

            String newFileName = this.fileService.renameFile(ofn);
            String path = fileService.resourceDirPath(newFileName, username, calendar);

            File saveFile = new File(this.fileService.getUploadDirectory()
                    + File.separator
                    + path);

            if (!saveFile.getParentFile().exists()) {
                if (!saveFile.getParentFile().mkdirs()) {
                    response.setErrno(HTTP_CODE.FAILED);
                }
            }


            try {
                mpf.transferTo(saveFile);
                // Linux 下面需要额外设置权限
                saveFile.setReadable(true, false);
            } catch (IOException e) {
                e.printStackTrace();
                throw new AppRestException("保存上传文件到服务器时出现IO错误", HTTP_CODE.EXCEPTION);
            }

            // 先不要了吧
            // UploadFile uploadFile = new UploadFile();
            // uploadFile.setPath(path);
            // uploadFile.setName(newFileName);

            // uploadFile = this.fileService.save(uploadFile);

            uploadFileList.add(fileService.getSystemProperties().getUploadFileUrl()
                    + "/"
                    + this.fileService.resourceUrl(newFileName, username, calendar));

        }

        response.setData(uploadFileList);
        return response;
    }

    // endregion

}
