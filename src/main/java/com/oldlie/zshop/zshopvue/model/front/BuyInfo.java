package com.oldlie.zshop.zshopvue.model.front;

import com.oldlie.zshop.zshopvue.model.db.Address;
import com.oldlie.zshop.zshopvue.model.db.ShoppingOrderItem;
import lombok.*;

import java.util.Collection;

@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class BuyInfo {
    private long orderId;
    private String sn;
    private Address address;
    private String totalPrice;
    private String balance;
    private Collection<ShoppingOrderItem> items;
}
