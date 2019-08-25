package com.oldlie.zshop.zshopvue.controller.backend;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.UploadFile;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@PropertySource("classpath:application.properties")
@RequestMapping("/backend/file")
@RestController
public class AdminFileController {

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    // region upload

    @PostMapping("/upload")
    public ListResponse<UploadFile> uploadFile(MultipartHttpServletRequest request,
                                               @SessionAttribute("username") String username,
                                               HttpServletResponse servletResponse) {
        ListResponse<UploadFile> response = new ListResponse<>();

        Iterator<String> itr = request.getFileNames();
        MultipartFile mpf;

        Calendar calendar = Calendar.getInstance();
        List<UploadFile> uploadFileList = new ArrayList<>();

        while (itr.hasNext()) {
            mpf = request.getFile(itr.next());
            StringBuilder fileNameBuilder = new StringBuilder(64);
            fileNameBuilder.append(UUID.randomUUID().toString()).append("_").append(mpf.getOriginalFilename());

            StringBuilder path = new StringBuilder(128);
            path.append(username).append(File.separator)
                    .append(calendar.get(Calendar.YEAR)).append(File.separator) // 上传年
                    .append(calendar.get(Calendar.MONTH)).append(File.separator) // 上传月
                    .append(fileNameBuilder.toString());

            File saveFile =
                    new File(this.fileService.getUploadDirectory() + File.separator + path.toString());

            if (!saveFile.getParentFile().exists()) {
                if (!saveFile.getParentFile().mkdirs()) {
                    response.setStatus(HTTP_CODE.FAILED);
                    response.setMessage("服务器没有成功的创建文件夹，请稍后再试");
                }
            }

            try {
                mpf.transferTo(saveFile);

                UploadFile uploadFile = new UploadFile();
                uploadFile.setPath(path.toString());
                uploadFile.setName(fileNameBuilder.toString());

                uploadFile = this.fileService.save(uploadFile);

                uploadFileList.add(uploadFile);
            } catch (IOException e) {
                e.printStackTrace();
                response.setStatus(HTTP_CODE.EXCEPTION);
                response.setMessage(e.getLocalizedMessage());
            }
        }

        response.setList(uploadFileList);
        return response;
    }

    @PostMapping("/image")
    public SimpleResponse<String> uploadImage(@RequestBody MultipartFile file,
                                              @SessionAttribute("username") String username) {
        SimpleResponse<String> response = new SimpleResponse<>();
        if (file.isEmpty()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("选择需要上传的文件");
            return response;
        }
        Calendar calendar = Calendar.getInstance();
        String fileName = file.getOriginalFilename();
        int length = fileName.length();
        if (fileName.length() > 15) {
            fileName = fileName.substring(length - 10, length);
        }
        StringBuilder fileNameBuilder = new StringBuilder(64);
        fileNameBuilder.append(calendar.getTime().getTime()).append("_").append(fileName);

        StringBuilder path = new StringBuilder(128);
        path.append(username).append(File.separator)
                .append(calendar.get(Calendar.YEAR)).append(File.separator) // 上传年
                .append(calendar.get(Calendar.MONTH)).append(File.separator) // 上传月
                .append(fileNameBuilder.toString());

        File savedFile =
                new File(this.fileService.getUploadDirectory() + File.separator + path.toString());

        if (!savedFile.getParentFile().exists()) {
            savedFile.getParentFile().mkdirs();
        }

        UploadFile uploadFile = new UploadFile();
        try {
            file.transferTo(savedFile);

            uploadFile.setPath(path.toString());
            uploadFile.setName(fileNameBuilder.toString());

            uploadFile = this.fileService.save(uploadFile);
            response.setItem(
                    this.fileService.getUploadURL() +
                            "/" +
                            uploadFile.getPath().replace("\\", "/"));

        } catch (IOException e) {
            response.setStatus(HTTP_CODE.EXCEPTION);
            response.setMessage(e.getLocalizedMessage());
        }

        return response;
    }

    // endregion

}
