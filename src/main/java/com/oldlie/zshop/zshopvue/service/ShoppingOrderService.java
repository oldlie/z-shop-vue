package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.cs.SHOPPING_ORDER_STATUS;
import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.db.repository.*;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

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
public class ShoppingOrderService {

    @Autowired
    private FinancialAccountRepository faRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingOrderRepository repository;

    @Autowired
    private ShoppingOrderItemRepository itemRepository;

    @Autowired
    private ShoppingOrderSerialRepository serialRepository;

    @Autowired
    private WalletRepository walletRepository;

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

    /**
     * 处理从购买页面来的直接下单的情况
     * @param uid user id
     * @param address address
     * @param commodityId commodity id
     * @param commodityTitle commodity title
     * @param formulaId formula id
     * @param formulaTitle formula title
     * @param formulaCount formula count
     * @param price price
     * @param status status
     * @return sn
     */
    @Transactional(rollbackFor = Exception.class)
    public SimpleResponse<String> shoppingOrder(long uid,
                                                 String address,
                                                 long commodityId,
                                                 String commodityTitle,
                                                 long formulaId,
                                                 String formulaTitle,
                                                 int formulaCount,
                                                 Money price,
                                                 int status) {
        SimpleResponse<String> response = new SimpleResponse<>();

        String sn = sn();

        ShoppingOrder order = ShoppingOrder.builder()
                .uid(uid)
                .serialNumber(sn)
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

        response.setItem(sn);

        return response;
    }

    /**
     * 处理从购物车页面下单的订单
     * @param uid user id
     * @param address address
     * @param items commodity item
     * @param status order status
     * @return sn
     */
    @Transactional(rollbackFor = Exception.class)
    public SimpleResponse<String> cartOrder(long uid, String address, List<ShoppingOrderItem> items, int status) {
        SimpleResponse<String> response = new SimpleResponse<>();
        String sn = sn();

        Money totalMoney = Money.parse("￥ 0.00");

        ShoppingOrder order = ShoppingOrder.builder()
                .uid(uid)
                .serialNumber(sn)
                .totalMoney(totalMoney)
                .status(status)
                .addressInfo(address)
                .build();

        order = this.repository.save(order);

        List<ShoppingOrderItem> list = new LinkedList<>();
        ObjectCopy<ShoppingOrderItem> copy = new ObjectCopy<>();
        for (ShoppingOrderItem item : items) {
            ShoppingOrderItem _item = new ShoppingOrderItem();
            _item = copy.copy(_item, item);
            _item.setShoppingOrderId(order.getId());
        }

        order.setItems(list);
        this.repository.save(order);

        response.setItem(sn);

        return response;
    }

    /**
     * 支付订单
     * @param uid user id
     * @param sn serial number
     * @return response
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse payOrder(long uid, String sn) {
        BaseResponse response = new BaseResponse();

        Wallet wallet = this.walletRepository.findOneByUid(uid);
        if (wallet == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("点券账户还没有初始化");
            return response;
        }

        ShoppingOrder order = this.repository.findByUidAndSerialNumber(uid, sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("你的订单已经不存在了");
            return response;
        }

        Money price = order.getTotalMoney();
        Money balance = wallet.getBalance();
        if (balance.isLessThan(price)) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("余额不足");
            return response;
        }

        balance = balance.minus(price);
        wallet.setBalance(balance);

        this.walletRepository.save(wallet);

        order.setStatus(SHOPPING_ORDER_STATUS.OUT_OF_WAREHOUSE);
        this.repository.save(order);

        // 取消订单的时候需要返还
        FinancialAccount ffa = this.faRepository.findFirstOrderByIdDesc();
        Money fab = ffa == null ? Money.parse("￥ 0.00") : ffa.getBalance();
        FinancialAccount fa = FinancialAccount.builder()
                .optUid(uid)
                .isAdd(FinancialAccount.INCOME)
                .money(price)
                .balance(fab.plus(price))
                .comment(uid + "购物增加系统收入：" + price.toString())
                .build();
        this.faRepository.save(fa);

        return response;
    }

    /**
     * 用户准备取消订单
     * @param uid user id
     * @param sn serial number
     * @return response
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse applyCancelOrder(long uid, String sn) {
        BaseResponse response = new BaseResponse();

        ShoppingOrder order = this.repository.findByUidAndSerialNumber(uid, sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("你的订单已经不存在了");
            return response;
        }

        order.setStatus(SHOPPING_ORDER_STATUS.APPLY_CANCEL);
        this.repository.save(order);

        return response;
    }

    /**
     * 管理员取消订单
     * @param uid user id
     * @param sn serial number
     * @return response
     */
    public BaseResponse cancelOrder(long adminId, long uid, String sn) {
        BaseResponse response = new BaseResponse();
        return response;
    }

    /**
     * 用户方完成订单
     * @param uid user id
     * @param sn serial number
     * @return response
     */
    public BaseResponse completeOrder(long uid, String sn) {
        BaseResponse response = new BaseResponse();
        ShoppingOrder order = this.repository.findByUidAndSerialNumber(uid, sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("你的订单已经不存在了");
            return response;
        }
        order.setStatus(SHOPPING_ORDER_STATUS.FINISHED);
        this.repository.save(order);
        return response;
    }
}
