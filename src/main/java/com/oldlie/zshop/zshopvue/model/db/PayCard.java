package com.oldlie.zshop.zshopvue.model.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_pay_card")
@ToString
public class PayCard extends BaseEO {

    @Column(columnDefinition = "varchar(255) comment '卡片序列号，尾号避免4'")
    private String serialNumber;
    @Column(columnDefinition = "tinyint default 1 comment '卡是否有效，默认有效为1，0后台无效，2兑换后无效'")
    private int isValid;
    @Column(columnDefinition = "bigint comment '卡片发行人/卡片制作人'")
    private Long publisherId;
    private String publisher;
    private String title;
    @Column(columnDefinition = "varchar(1000) comment '简要备注'")
    private String node;
    @Column(columnDefinition = "int default 360 comment '卡有效天数，默认360天'")
    private int validDay;
    @Column(columnDefinition = "char(32) comment '验证码'")
    private String verifyCode;
    // 面额
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money denomination;
    // 售价
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money amount;
    @Column(columnDefinition = "tinyint default 0 comment '是否已经销售'")
    private int isSoldOut;
    // 购买人
    private String customer;
    // 购买人电话
    private String customerPhone;
    // 使用人ID,卡片兑换者
    private Long uid;
    // 卡片兑换人姓名
    private String username;
    @Column(columnDefinition = "tinyint default 0 comment '是否已经兑换'")
    private int isExchanged;
    // 用户兑换日期
    private Date exchangedDate;
    // 用户最晚兑换日期，过期之后不能兑换
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date latestExchangeDate;
    private int ymd;
    private int cardCount;
    // 实际售价
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
}
