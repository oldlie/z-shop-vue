package com.oldlie.zshop.zshopvue.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_pay_card_log")
@ToString
public class PayCardLog extends BaseEO {
    private Long optId;
    private String optUsername;
    private String optDescription;
    private int opt;
    private long cardId;
}
