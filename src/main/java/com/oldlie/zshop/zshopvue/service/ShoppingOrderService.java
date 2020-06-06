package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.cs.SHOPPING_ORDER_STATUS;
import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.db.repository.*;
import com.oldlie.zshop.zshopvue.model.front.BuyInfo;
import com.oldlie.zshop.zshopvue.model.front.CommoditiesWithTag;
import com.oldlie.zshop.zshopvue.model.front.CommodityWithFormula;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ObjectCopy;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;

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
    private AddressRepository addressRepository;

    @Autowired
    private CommodityFormulaRepository cfRepositry;

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
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        int ymd = year * 10000 + month * 100 + date;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String hms = (hour > 9 ? "" : "0") + (hour * 10000 + minute * 100 + second);

        ShoppingOrderSerial serial = this.serialRepository.findByYmd(ymd);
        if (serial == null) {
            serial = ShoppingOrderSerial.builder()
                    .year(year)
                    .month(month)
                    .date(date)
                    .ymd(ymd)
                    .count(1)
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

        Collection<ShoppingOrderItem> items = order.getItems();
        List<CommodityFormula> cfList = new LinkedList<>();
        for (ShoppingOrderItem item : items) {
            Optional<CommodityFormula> optional = this.cfRepositry.findById(item.getFormulaId());
            if (!optional.isPresent()) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("商品【" + item.getCommodityTitle() + "】:"
                        + item.getFormulaTitle() + "规格不存在了，订单暂时无法处理。" );
                return response;
            }
            CommodityFormula cf = optional.get();
            int inventory = cf.getInventory() - item.getFormulaCount();
            if (inventory < 0) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("商品【" + item.getCommodityTitle() + "】:"
                        + item.getFormulaTitle() + "库存不足，订单暂时无法处理。" );
                return response;
            }
            cf.setInventory(inventory);
            cfList.add(cf);
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

        this.cfRepositry.saveAll(cfList);
        this.walletRepository.save(wallet);

        order.setStatus(SHOPPING_ORDER_STATUS.OUT_OF_WAREHOUSE);
        this.repository.save(order);

        // 取消订单的时候需要返还
        Money fab;
        Page<FinancialAccount> ffaList = this.faRepository.findAll(ZsTool.pageable(0, 1,"id", "desc"));
        if (ffaList.getTotalElements() > 0) {
            fab = ffaList.getContent().get(0).getBalance();
        } else {
            fab = Money.parse("￥ 0.00");
        }
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

    /**
     * 用户方获取自己的历史记录
     * @param uid user id
     * @param pageIndex page index
     * @param size size
     * @param orderBy order by
     * @param direct direct
     * @return shopping order history
     */
    public PageResponse<ShoppingOrder> history(long uid, int pageIndex, int size, String orderBy, String direct) {
        PageResponse<ShoppingOrder> response = new PageResponse<>();
        Page<ShoppingOrder> page = this.repository.findAllByUid(uid, ZsTool.pageable(pageIndex, size, orderBy, direct));
        response.setList(page.getContent());
        response.setTotal(page.getTotalElements());
        return response;
    }

    // region 管理员操作

    /**
     * 管理员查看所有的订单
     * 1, sn 模糊查询
     * 2， username 模糊查询
     * 3， commodity title 商品名称模糊查询
     * @param uid user id
     * @param sn serial number
     * @param commodityTitle commodity title
     * @param pageIndex page index
     * @param size size
     * @param orderBy order by
     * @param direct direct
     * @return page of shopping order
     */
    public PageResponse<ShoppingOrder> list(long uid, String sn, String commodityTitle, int pageIndex, int size,
                                            String orderBy, String direct) {
        PageResponse<ShoppingOrder> response = new PageResponse<>();
        Page<ShoppingOrder> page;
        // TODO:
        // 暂时不考虑商品名称了  commodity，因为不知道怎么查
        if (StringUtils.isNotEmpty(sn)) {
            page = this.repository.findAll(
                    (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("serialNumber"), sn),
                    ZsTool.pageable(pageIndex, size, orderBy, direct)
            );
        } else {
            page = this.repository.findAll(ZsTool.pageable(pageIndex, size, orderBy, direct));
        }
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }

    /**
     * 发货
     * @param uid user id
     * @param id id
     * @param psn post serial number
     * @param pc post company
     * @return base response
     */
    public BaseResponse delivery(long uid, long id, String psn, String pc) {
        BaseResponse response = new BaseResponse();

        Optional<ShoppingOrder> opt = this.repository.findById(id);
        if (!opt.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("订单不存在了");
            return response;
        }
        ShoppingOrder order = opt.get();
        order.setStatus(SHOPPING_ORDER_STATUS.DELIVERING);
        order.setPostSerialNumber(psn);
        order.setPostCompany(pc);
        this.repository.save(order);
        return response;
    }

    /**
     * 取消订单
     * @param uid
     * @param id
     * @param reason
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse cancel(long uid, long id, String reason) {
        BaseResponse response = new BaseResponse();

        Optional<ShoppingOrder> opt = this.repository.findById(id);
        if (!opt.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("订单不存在了");
            return response;
        }

        ShoppingOrder order = opt.get();
        order.setStatus(SHOPPING_ORDER_STATUS.CANCELED);
        order.setCancelReason(reason);
        this.repository.save(order);

        Money money = order.getTotalMoney();

//        FinancialAccount last = this.faRepository.findTop1(ZsTool.sort("id", "desc"));
//
//        FinancialAccount fa = FinancialAccount.builder()
//                .optUid(uid)
//                .money(money)
//                .balance(last.getBalance().minus(money))
//                .isAdd(FinancialAccount.EXPENSE)
//                .comment(uid + " 取消订单，支出：" + money.toString())
//                .build();
//        this.faRepository.save(fa);

        return response;
    }

    // endregion


    /**
     * 通过订单号获取客户方的订单信息，以便客户方进行订单结算
     * @param uid user id
     * @param sn serial number
     * @return buy information
     */
    public SimpleResponse<BuyInfo> buyInfo(long uid, String sn) {
        SimpleResponse<BuyInfo> response = new SimpleResponse<>();
        ShoppingOrder order = this.repository.findByUidAndSerialNumber(uid, sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("没有找到指定的订单");
            return response;
        }
        Collection<ShoppingOrderItem> items = order.getItems();
        Money total = Money.parse("CNY 0.00");
        for (ShoppingOrderItem item : items) {
            total = total.plus(item.getPrice().multipliedBy(item.getFormulaCount()));
        }
        Address da = this.addressRepository.findOneByUidAndIsDefault(uid, 1);

        String balance = "";
        Wallet wallet = this.walletRepository.findOneByUid(uid);
        if (wallet != null) {
           balance = wallet.getBalance().toString();
        }

        response.setItem(
                BuyInfo.builder()
                        .orderId(order.getId())
                        .sn(sn)
                        .address(da)
                        .items(items)
                        .totalPrice(total.toString().replace("CNY ", ""))
                        .balance(balance.replace("CNY ", ""))
                        .build()
        );
        return response;
    }
}
