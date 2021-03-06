package com.oldlie.zshop.zshopvue.model.db;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 这个类其实没什么必要，因为也管不了
 */
@Entity
@Data
@Table(name = "t_upload_file")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class UploadFile extends BaseEo {
    private String path;
    private String name;
    private int fileType;
}
