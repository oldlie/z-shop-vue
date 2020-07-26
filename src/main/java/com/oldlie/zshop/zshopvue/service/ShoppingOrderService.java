package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.*;
import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.db.repository.*;
import com.oldlie.zshop.zshopvue.model.front.BuyInfo;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
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

import javax.persistence.criteria.CriteriaBuilder;
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
    private CommodityCommentRepository ccRepository;

    @Autowired
    private CommodityRepository commodityRepository;

    @Autowired
    private CommodityFormulaRepository cfRepository;

    @Autowired
    private EvaDimRepository evaDimRepository;

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
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    /**
     * 获取订单号
     * @return shopping order serial number
     */
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
     * @param formulaId formula id
     * @param formulaCount formula count
     * @param status status
     * @return sn
     */
    @Transactional(rollbackFor = Exception.class)
    public SimpleResponse<String> shoppingOrder(long uid,
                                                 String address,
                                                 long commodityId,
                                                 long formulaId,
                                                 int formulaCount,
                                                 int status) {
        SimpleResponse<String> response = new SimpleResponse<>();

        String sn = sn();

        Commodity commodity = this.commodityRepository.findOneById(commodityId);
        if (commodity == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("商品已经不存在了");
            return response;
        }
        if (COMMODITY_STATUS.OFFLINE == commodity.getStatus()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("商品已经下架了");
            return response;
        }
        CommodityFormula formula = this.cfRepository.findOneByIdAndCommodityId(formulaId, commodityId);
        if (formula == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("你选的规格已经不存在了");
            return response;
        }
        if (formula.getInventory() < formulaCount) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("你选的规格库存不足");
            return response;
        }
        Money total = formula.getPrice().multipliedBy(formulaCount);

        ShoppingOrder order = ShoppingOrder.builder()
                .uid(uid)
                .serialNumber(sn)
                .totalMoney(total)
                .status(status)
                .addressInfo(address)
                .build();

        order = this.repository.save(order);

        ShoppingOrderItem item = ShoppingOrderItem.builder()
                .shoppingOrderId(order.getId())
                .commodityId(commodityId)
                .commodityTitle(commodity.getTitle())
                .commodityImage(commodity.getThumbnail())
                .formulaId(formulaId)
                .formulaTitle(formula.getTitle())
                .formulaCount(formulaCount)
                .price(formula.getPrice())
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
    public BaseResponse payOrder(long uid, String sn, String payPassword, String address) {
        BaseResponse response = new BaseResponse();

        Optional<User> optionalUser = this.userRepository.findById(uid);
        if (!optionalUser.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("当前账户不存在了，请重新登录");
            return response;
        }

        User user = optionalUser.get();
        if (!user.getPayPassword().equals(payPassword)) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("支付密码错误");
            return response;
        }

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
        order.setAddressInfo(address);
        Collection<ShoppingOrderItem> items = order.getItems();
        List<CommodityFormula> cfList = new LinkedList<>();
        for (ShoppingOrderItem item : items) {
            Optional<CommodityFormula> optional = this.cfRepository.findById(item.getFormulaId());
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

        this.cfRepository.saveAll(cfList);
        this.walletRepository.save(wallet);

        order.setStatus(SHOPPING_ORDER_STATUS.OUT_OF_WAREHOUSE);
        this.repository.save(order);

        // 取消订单的时候需要返还
        Money fab;
        Page<FinancialAccount> ffaList = this.faRepository.findAll(ZsTool.pageable(0, 1,"id", "desc"));
        if (ffaList.getTotalElements() > 0) {
            fab = ffaList.getContent().get(0).getBalance();
        } else {
            fab = Money.parse("CNY 0.00");
        }
        FinancialAccount fa = FinancialAccount.builder()
                .optUid(uid)
                .isAdd(FinancialAccount.INCOME)
                .money(price)
                .balance(fab.plus(price))
                .comment("用户" + user.getUsername() + "[" + uid + "]支付订单：" + sn + "收入：" + price.toString())
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
        // 用户点击完成之后订单转为等待评价状态，前端跳转到评价页面
        order.setStatus(SHOPPING_ORDER_STATUS.WAITING_COMMENT);
        this.repository.save(order);
        return response;
    }

    /**
     * 获取待支付的订单信息。默认取最后的30条信息
     * @param uid user id
     * @return list of shopping order
     */
    public ListResponse<ShoppingOrder> listForWaitingPay(long uid) {
        ListResponse<ShoppingOrder> response = new ListResponse<>();
        Page<ShoppingOrder> page =this.repository.findAll(
                (root, query, criteriaBuilder) -> {
                    Predicate predicate1 = criteriaBuilder.equal(root.get("uid"), uid);
                    Predicate predicate2 = criteriaBuilder.equal(root.get("status"), SHOPPING_ORDER_STATUS.PREPARING);
                    return criteriaBuilder.and(predicate1, predicate2);
                },
                ZsTool.pageable(1, 30)
        );
        response.setList(page.getContent());
        return response;
    }

    /**
     * 获取待支付的订单信息。默认取最后的30条信息
     * @param uid user id
     * @return list of shopping order
     */
    public ListResponse<ShoppingOrder> listForWaitingComment(long uid) {
        ListResponse<ShoppingOrder> response = new ListResponse<>();
        Page<ShoppingOrder> page =this.repository.findAll(
                (root, query, criteriaBuilder) -> {
                    Predicate predicate1 = criteriaBuilder.equal(root.get("uid"), uid);
                    Predicate predicate2 = criteriaBuilder.equal(root.get("status"),
                            SHOPPING_ORDER_STATUS.WAITING_COMMENT);
                    return criteriaBuilder.and(predicate1, predicate2);
                },
                ZsTool.pageable(1, 30)
        );
        response.setList(page.getContent());
        return response;
    }



    /**
     * 获取用户的正在出库和快递中的订单列表
     * @param uid user id
     * @param index index of page
     * @param size size of page
     * @return list of shopping order
     */
    public PageResponse<ShoppingOrder> listBeforeConfirmed(long uid, int index, int size) {
        PageResponse<ShoppingOrder> response = new PageResponse<>();
        Page<ShoppingOrder> page = this.repository.findAll(
                (root, query, criteriaBuilder) -> {
                    Predicate predicate1 = criteriaBuilder.equal(root.get("uid"), uid);
                    Predicate predicate2 = criteriaBuilder.equal(root.get("status"), SHOPPING_ORDER_STATUS.OUT_OF_WAREHOUSE);
                    Predicate predicate3 = criteriaBuilder.equal(root.get("status"), SHOPPING_ORDER_STATUS.DELIVERING);
                    Predicate predicate4 = criteriaBuilder.or(predicate2, predicate3);
                    return criteriaBuilder.and(predicate1, predicate4);
                },
                ZsTool.pageable(index, size)
        );
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
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
        Page<ShoppingOrder> page = this.repository.findAll(
                (root, query, cb) -> {
                    Predicate predicate1 = cb.equal(root.get("uid"), uid);
                    Predicate predicate2 = cb.ge(root.get("status"), SHOPPING_ORDER_STATUS.RECEIVED);
                    return cb.and(predicate1, predicate2);
                },
                ZsTool.pageable(pageIndex, size, orderBy, direct)
        );
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
     * 用户新发起的订单
     * @param index page index
     * @param size page size
     * @return Page of ShoppingOrder
     */
    public PageResponse<ShoppingOrder> newOrders(int index, int size) {
        PageResponse<ShoppingOrder> response = new PageResponse<>();
        Page<ShoppingOrder> page = this.repository.findAll(
                (root, query, cb) -> cb.equal(root.get("status"), SHOPPING_ORDER_STATUS.OUT_OF_WAREHOUSE),
                ZsTool.pageable(index, size)
        );
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }

    /**
     * 在途的订单，即已经添加了快递信息的订单
     * @param index page index
     * @param size page size
     * @return Page of ShoppingOrder
     */
    public PageResponse<ShoppingOrder> onWayOrder(int index, int size) {
        PageResponse<ShoppingOrder> response = new PageResponse<>();
        Page<ShoppingOrder> page = this.repository.findAll(
                (root, query, cb) -> cb.equal(root.get("status"), SHOPPING_ORDER_STATUS.DELIVERING),
                ZsTool.pageable(index, size)
        );
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
     * 用户自己在结算支付前取消订单，不涉及用户以及系统账户的金额变动
     * @param uid user id
     * @param sn serial number
     * @param reason reason
     * @return response
     */
    public BaseResponse cancelBeforePay(long uid, String sn, String reason) {
        BaseResponse response = new BaseResponse();
        ShoppingOrder order = this.repository.findByUidAndSerialNumber(uid, sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("订单不存在了");
            return response;
        }
        order.setStatus(SHOPPING_ORDER_STATUS.CANCELED);
        order.setCancelReason(reason);
        this.repository.save(order);

        return response;
    }

    /**
     * 在支付之用户后取消订单，支付之后只有管理员能取消订单
     * @param uid user id
     * @param sn serial number
     * @param reason reason
     * @return response
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse cancel(long uid, String sn, String reason) {
        BaseResponse response = new BaseResponse();

        ShoppingOrder order = this.repository.findBySerialNumber(sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("这个订单不存在了");
            return response;
        }
        order.setStatus(SHOPPING_ORDER_STATUS.CANCELED);
        order.setCancelReason(reason);
        this.repository.save(order);

        Wallet wallet = this.walletRepository.findOneByUid(order.getUid());
        if (wallet == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("你还没有支付账户，不能取消订单，请联系客户处理");
            return response;
        }

        Money money = order.getTotalMoney();
        Page<FinancialAccount> page = this.faRepository.findAll(ZsTool.pageable(0, 3));
        if (page.getTotalElements() < 0) {
            FinancialAccount fa = FinancialAccount.builder()
                    .optUid(uid)
                    .money(money)
                    .balance(money)
                    .isAdd(FinancialAccount.EXPENSE)
                    .comment("管理员" + uid + " 取消订单：" + sn + "，支出：" + money.toString())
                    .build();
            this.faRepository.save(fa);
        } else {
            List<FinancialAccount> list = page.getContent();
            FinancialAccount last = list.get(0);
            FinancialAccount fa = FinancialAccount.builder()
                    .optUid(uid)
                    .money(money)
                    .balance(last.getBalance().minus(money))
                    .isAdd(FinancialAccount.EXPENSE)
                    .comment("管理员" + uid + " 取消订单：" + sn + "，支出：" + money.toString())
                    .build();
            this.faRepository.save(fa);
        }

        wallet.setBalance(wallet.getBalance().plus(money));
        this.walletRepository.save(wallet);

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
                        .status(order.getStatus())
                        .address(da)
                        .items(items)
                        .totalPrice(total.toString().replace("CNY ", ""))
                        .balance(balance.replace("CNY ", ""))
                        .build()
        );
        return response;
    }


    /**
     * 通过用户ID和订单SN获取用户当前订单的详细信息
     * @param uid user id
     * @param sn serial number
     * @return detail information
     */
    public SimpleResponse<Map<String, Object>> orderForComment(long uid, String sn) {
        SimpleResponse<Map<String, Object>> response = new SimpleResponse<>();
        ShoppingOrder order = this.repository.findByUidAndSerialNumber(uid, sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("即将评价的订单不存在了");
            return response;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("sn", sn);
        Collection<ShoppingOrderItem> orderItems = order.getItems();
        List<Map<String, Object>> list = new LinkedList<>();
        for (ShoppingOrderItem item : orderItems) {
            Map<String, Object> it = new HashMap<>();
            it.put("cid", item.getCommodityId());
            it.put("commodity", item.getCommodityTitle());
            it.put("image", item.getCommodityImage());
            List<EvaluativeDimension> dimensions = this.evaDimRepository.findAll(
                    (root, criteriaQuery, cb) -> cb.equal(root.get("cid"), item.getCommodityId())
            );
            List<Map<String, Object>> dimList = new LinkedList<>();
            for (EvaluativeDimension dimension : dimensions) {
                Map<String, Object> evaDim = new HashMap<>();
                evaDim.put("id", dimension.getId());
                evaDim.put("title", dimension.getTitle());
                dimList.add(evaDim);
            }
            it.put("dimensions",dimList);
            list.add(it);
        }
        map.put("commodities", list);
        response.setItem(map);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse comment(long uid, String username,
                                long cid, String sn, int rate, String quickIds, String comment) {
        BaseResponse response = new BaseResponse();

        if (StringUtils.isEmpty(comment) && StringUtils.isEmpty(quickIds)) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("请填写评论");
            return response;
        }

        if (comment.length() > 140) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("评论不要超过140字符");
            return response;
        }

        ShoppingOrder order = this.repository.findByUidAndSerialNumber(uid, sn);
        if (order == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("订单不存在了，请刷新重试");
            return response;
        }

        long count = this.ccRepository.count(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get("uid"), uid);
                    Predicate predicate1 = criteriaBuilder.equal(root.get("cid"), cid);
                    Predicate predicate2 = criteriaBuilder.equal(root.get("oid"), order.getId());
                    return criteriaBuilder.and(predicate, predicate1, predicate2);
                }
        );
        if (count > 0) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("订单中的商品已经评价过了，请勿重复评价");
            return response;
        }

        Commodity commodity = this.commodityRepository.findOneById(cid);
        if (comment == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("订单中的商品不存在了，请刷新重试");
            return response;
        }
        commodity.setSellCount(commodity.getSellCount() + 1);
        commodity.setCommentCount(commodity.getCommentCount() + 1);
        commodity.setCommentScore(commodity.getCommentScore() + 5 + rate);
        this.commodityRepository.save(commodity);

        StringBuilder builder = new StringBuilder(128);
        if (StringUtils.isNotEmpty(quickIds)) {
            List<EvaluativeDimension> dimensions = this.evaDimRepository.findAll(
                    (root, criteriaQuery, cb) -> cb.equal(root.get("cid"), cid)
            );
            dimensions.forEach(x -> {
                if (quickIds.contains(x.getId() + ",")) {
                    x.setCount(x.getCount() + 1);
                    builder.append(x.getTitle()).append(",");
                }
            });
            this.evaDimRepository.saveAll(dimensions);
        }

        CommodityComment cc = new CommodityComment();
        cc.setUid(uid);
        cc.setNickname(username);
        cc.setOid(order.getId());
        cc.setSn(sn);
        cc.setCid(cid);
        cc.setEvaDim(builder.toString());
        cc.setEvaText(comment);
        cc.setStatus(0);
        this.ccRepository.save(cc);

        Collection<ShoppingOrderItem> items = order.getItems();
        boolean allCommodityCommented = true;
        for (ShoppingOrderItem item : items) {
            if (item.getCommodityId() == cid) {
                item.setStatus(1);
            } else {
                allCommodityCommented = allCommodityCommented && (item.getStatus() == 1);
            }
        }
        this.itemRepository.saveAll(items);

        if (allCommodityCommented) {
            // 所有的商品都被评价了的话，那这个订单就算是被评价了
            order.setStatus(SHOPPING_ORDER_STATUS.FINISHED);
        }
        return response;
    }
}
