package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.config.properties.SystemProperties;
import com.oldlie.zshop.zshopvue.model.db.UploadFile;
import com.oldlie.zshop.zshopvue.model.db.repository.UploadFileRepository;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

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

    public String resourceDirPath(String fileName, String username, Calendar calendar) {
        StringBuilder path = new StringBuilder(128);
        String pathPrefix = DigestUtils.md5Hex(username).substring(0, 2);

        path.append(pathPrefix).append(File.separator)
                .append(username).append(File.separator)
                .append(calendar.get(Calendar.YEAR)).append(File.separator) // 上传年
                .append(calendar.get(Calendar.MONTH)).append(File.separator) // 上传月
                .append(fileName);

        return path.toString();
    }

    public String resourceUrl(String fileName, String username, Calendar calendar) {
        StringBuilder path = new StringBuilder(128);
        String pathPrefix = DigestUtils.md5Hex(username).substring(0, 2);

        path.append(pathPrefix).append("/")
                .append(username).append("/")
                .append(calendar.get(Calendar.YEAR)).append("/") // 上传年
                .append(calendar.get(Calendar.MONTH)).append("/") // 上传月
                .append(fileName);

        return path.toString();
    }

    public String renameFile(String fileName) {
        int lastPoint = fileName.lastIndexOf(".");
        String suffix = ".jpg";
        if (lastPoint >= 0) {
            suffix = fileName.substring(lastPoint);
        }
        return UUID.randomUUID().toString() + suffix;
    }
}
