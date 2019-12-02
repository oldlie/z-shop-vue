package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.config.properties.SystemProperties;
import com.oldlie.zshop.zshopvue.model.db.UploadFile;
import com.oldlie.zshop.zshopvue.model.db.repository.UploadFileRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Getter
    private SystemProperties systemProperties;

    @Autowired
    public void setSystemProperties(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
    }

    private UploadFileRepository uploadFileRepository;

    @Autowired
    public void setUploadFileRepository(UploadFileRepository uploadFileRepository) {
        this.uploadFileRepository = uploadFileRepository;
    }

    public String getUploadDirectory() {
        return this.systemProperties.getFileUploadTemp();
    }

    public String getUploadURL() {
        return this.systemProperties.getUploadFileUrl();
    }

    public UploadFile save(UploadFile file) {
        return this.uploadFileRepository.save(file);
    }
}
