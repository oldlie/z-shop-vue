package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UploadFileRepostitory extends JpaRepository<UploadFile, Long>, JpaSpecificationExecutor<UploadFile> {
}
