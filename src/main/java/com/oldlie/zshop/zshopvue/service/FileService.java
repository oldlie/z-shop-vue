package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.UploadFile;
import com.oldlie.zshop.zshopvue.model.db.repository.UploadFileRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Value("${fileUploadTemp}")
    private String uploadDirectory;

    @Value("${uploadFileUrl}")
    private String uploadURL;

    private UploadFileRepostitory uploadFileRepostitory;

    @Autowired
    public void setUploadFileRepostitory(UploadFileRepostitory uploadFileRepostitory) {
        this.uploadFileRepostitory = uploadFileRepostitory;
    }

    public String getUploadDirectory() {
        return this.uploadDirectory;
    }

    public String getUploadURL() {
        return this.uploadURL;
    }

    public UploadFile save(UploadFile file) {
        return this.uploadFileRepostitory.save(file);
    }
}
