package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrder;
import com.oldlie.zshop.zshopvue.model.db.ShoppingOrderItem;
import com.oldlie.zshop.zshopvue.model.db.ShoppingOrderSerial;
import com.oldlie.zshop.zshopvue.model.db.repository.ShoppingCartRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.ShoppingOrderItemRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.ShoppingOrderRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.ShoppingOrderSerialRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Process shopping order.
 * 1, direct order from buy page.
 * 2, shopping cart order.
 *
 * @author CL
 * @date 2020/5/26
 */
@Service
@Slf4j
public class ShopOrderService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingOrderRepository repository;

    @Autowired
    private ShoppingOrderItemRepository itemRepository;

    @Autowired
    private ShoppingOrderSerialRepository serialRepository;

    @Transactional(rollbackFor = Exception.class)
    private String sn() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int ymd = year * 10000 + month * 100 + date;

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int hms = hour * 10000 + minute * 100 + second;

        ShoppingOrderSerial serial = this.serialRepository.findByYmd(ymd);
        if (serial == null) {
            serial = ShoppingOrderSerial.builder()
                    .year(year)
                    .month(month)
                    .date(date)
                    .ymd(ymd)
                    .count(0)
                    .build();
        }

        int count = 1000000 + serial.getCount();
        String countStr = String.valueOf(count).substring(1, 7);

        serial.setCount(serial.getCount() + 1);
        this.serialRepository.save(serial);

        return ymd + hms + countStr;
    }

    @Transactional(rollbackFor = Exception.class)
    public SimpleResponse<String>  shoppingOrder(long uid,
                                                 String address,
                                                 long commodityId,
                                                 String commodityTitle,
                                                 long formulaId,
                                                 String formulaTitle,
                                                 int formulaCount,
                                                 Money price,
                                                 int status) {
        SimpleResponse<String> response = new SimpleResponse<>();

        ShoppingOrder order = ShoppingOrder.builder()
                .uid(uid)
                .serialNumber(sn())
                .totalMoney(price.multipliedBy(formulaCount))
                .status(status)
                .addressInfo(address)
                .build();

        order = this.repository.save(order);

        ShoppingOrderItem item = ShoppingOrderItem.builder()
                .shoppingOrderId(order.getId())
                .commodityId(commodityId)
                .commodityTitle(commodityTitle)
                .formulaId(formulaId)
                .formulaTitle(formulaTitle)
                .formulaCount(formulaCount)
                .price(price)
                .status(status)
                .build();

        this.itemRepository.save(item);

        return response;
    }
}
