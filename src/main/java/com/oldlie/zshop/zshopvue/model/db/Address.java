package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_address")
@ToString
public class Address extends BaseEO {

    private Long uid;
    @Column(columnDefinition = "tinyint default 0 comment '是否是默认地址'")
    private int isDefault;
    private String province;
    private String city;
    private String county;
    private String address;
    private String contactName;
    private String contactPhone;

}
