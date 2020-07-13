package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.cs.MONEY_EXCHANGE;
import com.oldlie.zshop.zshopvue.model.cs.PAY_CARD_OPT;
import com.oldlie.zshop.zshopvue.model.db.*;
import com.oldlie.zshop.zshopvue.model.db.repository.*;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author oldlie
 */
@Slf4j
@Service
public class PayCardService {

    private PayCardRepository payCardRepository;
    private PayCardLogRepository payCardLogRepository;

    @Autowired
    private ExchangeRecordRepository erRepository;
    @Autowired
    private UserCardRepository userCardRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    public void setPayCardRepository(PayCardRepository payCardRepository) {
        this.payCardRepository = payCardRepository;
    }

    @Autowired
    public void setPayCardLogRepository(PayCardLogRepository payCardLogRepository) {
        this.payCardLogRepository = payCardLogRepository;
    }

    /**
     * 创建一张兑换卡
     * @param title card title
     * @param note node
     * @param validDay 有效期
     * @param denomination 面额
     * @param amount 售价
     * @param latestExchangeDate 卡片最晚兑换日期
     * @param uid 创建卡片的账号ID
     * @param username 创建卡片时账号的名称
     * @return BaseResponse
     */
    public BaseResponse payCard(String title, String note, int validDay, String denomination, String amount,
                                String latestExchangeDate, long uid, String username) {
        BaseResponse response = new BaseResponse();
        try {
            long id = createPayCard(title, note, validDay, denomination, amount, latestExchangeDate, uid, username);
        } catch (ParseException e) {
            e.printStackTrace();
            response.setMessage("时间转换错误");
            response.setStatus(HTTP_CODE.EXCEPTION);
        }
        return response;
    }

    /**
     * 批量创建兑换卡
     * @param title card title
     * @param node node
     * @param validDay valid day
     * @param denomination denomination
     * @param amount sold amount
     * @param latestExchangeDate latest exchange date
     * @param uid user id who creates card
     * @param username user name when the card created
     * @param count card count
     * @return BaseResponse
     */
    public BaseResponse payCards(String title, String node, int validDay, String denomination, String amount,
                                 String latestExchangeDate, long uid, String username, int count) {
        BaseResponse response = new BaseResponse();
        for (int i = 0; i < count; i++) {
            try {
                createPayCard(title, node, validDay, denomination, amount, latestExchangeDate, uid, username);
            } catch (ParseException e) {
                e.printStackTrace();
                response.setStatus(HTTP_CODE.EXCEPTION);
                response.setMessage("时间转换错误");
                return response;
            }
        }
        return response;
    }

    @Transactional
    private long createPayCard(String title, String note, int validDay, String denomination, String amount,
                                String latestExchangeDate, long uid, String username) throws ParseException {
        PayCard payCard = new PayCard();
        Calendar calendar = Calendar.getInstance();
        int ymd = calendar.get(Calendar.YEAR) * 10000
                + (calendar.get(Calendar.MONTH) + 1) * 100
                + calendar.get(Calendar.DATE);
        Page<PayCard> payCardPage = this.payCardRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("ymd"), ymd),
                ZsTool.pageable(0, 10, "id", "desc")
        );
        List<PayCard> list = payCardPage.getContent();
        int serialNumber = 1;
        if (list.size() > 0) {
            PayCard latest = list.get(0);
            serialNumber = latest.getCardCount() + 1;
        }
        if ( (serialNumber - 4) % 10 == 0) {
            serialNumber = serialNumber + 1;
        }
        String sn = ymd + "" + String.format("%06d", serialNumber);
        payCard.setSerialNumber(sn);

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        int dash = uuidString.indexOf("-");
        String verifyCode = uuidString.substring(0, dash);
        payCard.setVerifyCode(verifyCode);
        payCard.setTitle(title);
        payCard.setNode(note);
        payCard.setValidDay(validDay);
        payCard.setDenomination(Money.parse(denomination));
        payCard.setAmount(Money.parse(amount));
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DATE, validDay);
        payCard.setLatestExchangeDate(calendar.getTime());
        payCard.setIsValid(1);
        payCard.setPublisherId(uid);
        payCard.setPublisher(username);
        payCard.setIsSoldOut(0);
        payCard.setCustomer("");
        payCard.setCustomerPhone("");
        payCard.setUid(0L);
        payCard.setUsername("");
        payCard.setIsExchanged(0);
        payCard.setExchangedDate(null);
        payCard.setYmd(ymd);
        payCard.setCardCount(serialNumber);

        payCard = this.payCardRepository.save(payCard);

        PayCardLog log = new PayCardLog();
        log.setCardId(payCard.getId());
        log.setOptDescription(PAY_CARD_OPT.CREATE);
        log.setOpt(PAY_CARD_OPT.CREATE_);
        log.setOptId(uid);
        log.setOptUsername(username);
        this.payCardLogRepository.save(log);

        return payCard.getId();
    }

    @Transactional
    public BaseResponse delete(final long id, final long uid, final String username) {
        BaseResponse response = new BaseResponse();
        this.payCardRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id)
        ).ifPresent(x -> {
            PayCardLog log = new PayCardLog();
            log.setCardId(x.getId());
            log.setOptId(uid);
            log.setOptUsername(username);
            log.setOpt(PAY_CARD_OPT.DELETE_);
            log.setOptDescription(PAY_CARD_OPT.DELETE);
            this.payCardRepository.delete(x);
        });

        return response;
    }

    public PageResponse<PayCard> page(String key, Object value, int index, int size, String orderBy, String order) {
        PageResponse<PayCard> response = new PageResponse<>();
        Page<PayCard> page;
        if (key == null) {
             page = this.payCardRepository.findAll(
                    ZsTool.pageable(index, size, orderBy, order)
            );
        } else {
            page = this.payCardRepository.findAll(
                    (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(key), value),
                    ZsTool.pageable(index, size, orderBy, order)
            );
        }
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }

    public SimpleResponse<PayCard> payCard(final long id) {
        SimpleResponse<PayCard> response = new SimpleResponse<>();
        Optional<PayCard> optional = this.payCardRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id)
        );
        if (!optional.isPresent()) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("您要查看的礼品卡已经不存在了");
            return response;
        }
        response.setItem(optional.get());
        return response;
    }

    /**
     * 出售卡片
     * @param idStr ids's string
     * @param customer customer name
     * @param customerPhone customer phone
     * @param amount amount
     * @param customerId customer id
     * @param assign assign system user
     * @param uid user id of sold
     * @param username username of sold
     * @return BaseResponse
     */
    @Transactional
    public BaseResponse sold(final String idStr,
                             final String customer,
                             final String customerPhone,
                             final String amount,
                             final long customerId,
                             final int assign,
                             final long uid,
                             final String username) {
        BaseResponse response = new BaseResponse();
        String[] ids = idStr.split(",");
        List<UserCard> userCardList = new LinkedList<>();
        List<PayCard> payCardList = new LinkedList<>();
        List<PayCardLog> payCardLogList = new LinkedList<>();
        for (String _id : ids) {
            long id = Long.parseLong(_id);
            Optional<PayCard> optional = this.payCardRepository.findById(id);
            if (!optional.isPresent()) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("卡片不存在了，请刷新重试");
                return response;
            }
            PayCard payCard = optional.get();

            if (payCard.getIsValid() == 0) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("无效卡，不能分配");
                return response;
            }

            long now = Calendar.getInstance().getTimeInMillis();
            long expires = payCard.getLatestExchangeDate().getTime();
            if (now > expires) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("过期卡，不能分配");
                return response;
            }
            if (payCard.getIsExchanged() == 1) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("已兑换了的卡，不能分配");
                return response;
            }

            payCard.setCustomer(customer);
            payCard.setCustomerPhone(customerPhone);
            payCard.setPrice(Money.parse(amount));
            payCard.setIsSoldOut(1);
            payCardList.add(payCard);
            if (assign == 1) {
                userCardList.add(UserCard.builder()
                        .uid(customerId)
                        .cardId(id)
                        .build());
            }
            payCardLogList.add(PayCardLog.builder()
                    .cardId(id)
                    .optDescription(PAY_CARD_OPT.SOLD)
                    .optUsername(username)
                    .optId(uid)
                    .opt(PAY_CARD_OPT.SOLD_)
                    .build());
       }

        this.payCardRepository.saveAll(payCardList);
        this.payCardLogRepository.saveAll(payCardLogList);

        if (assign == 1) {
            // 直接将卡片分配到系统内的账户
            this.userCardRepository.saveAll(userCardList);
        }

        return response;
    }

    // region 个人操作自己的兑换卡

    /**
     * 当前只显示已经充值了的卡片信息
     * @param uid user id
     * @param pageIndex page index
     * @param size size
     * @param orderBy order by
     * @param direct direct
     * @return one page of pay cards
     */
    public PageResponse<PayCard> listMyPayCards(long uid, int pageIndex, int size, String orderBy, String direct) {
        PageResponse<PayCard> response = new PageResponse<>();
        Page<PayCard> payCardPage =
                this.payCardRepository.findAllByUid(uid, ZsTool.pageable(pageIndex, size, orderBy, direct));
        response.setTotal(payCardPage.getTotalElements());
        response.setList(payCardPage.getContent());
        return response;
    }

    /**
     * 用户兑换一张兑换卡
     * @param uid user id
     * @param sn serial number
     * @param password verify code
     * @return balance
     */
    @Transactional
    public SimpleResponse<Money> payCardRecharge(long uid, String username, String sn, String password) {
        SimpleResponse<Money> response = new SimpleResponse<>();
        PayCard card = this.payCardRepository.findOneBySerialNumberAndVerifyCode(sn, password);
        if (card == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("没有该兑换卡的信息，请检查序列号和密码是否输入正确。");
            return response;
        }
        if (card.getIsValid() != 1) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("无效卡片。");
            return response;
        }
        long now = Calendar.getInstance().getTimeInMillis();
        long expires = card.getLatestExchangeDate().getTime();
        if (now > expires) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("过期卡片。");
            return response;
        }
        if (card.getIsExchanged() == 1) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("卡片已经兑换过了");
            return response;
        }

        Wallet wallet = this.walletRepository.findOneByUid(uid);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUid(uid);
        }
        Money balance = wallet.getBalance();
        if (balance == null) {
            balance = card.getDenomination();
        } else {
            // 和面值相加，而不是实际的金额
            balance = balance.plus(card.getDenomination());
        }

        wallet.setBalance(balance);
        // 增加账户余额
        this.walletRepository.save(wallet);

        Date exchangeDate = new Date();

        card.setUid(uid);
        card.setUsername(username);
        card.setIsExchanged(1);
        card.setExchangedDate(exchangeDate);
        // 变更卡片兑换者ID,账号，兑换状态和兑换时间
        this.payCardRepository.save(card);

        PayCardLog log = PayCardLog.builder()
                .cardId(card.getId())
                .optUsername(username)
                .optId(uid)
                .opt(PAY_CARD_OPT.USER_EXCHANGED_)
                .optDescription(PAY_CARD_OPT.USER_EXCHANGE)
                .build();
        // 记录卡片变更概要信息
        this.payCardLogRepository.save(log);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String comment = format.format(new Date())
                +  " 兑换卡充值:"+ card.getDenomination().getAmountMajorLong()
                + " 兑换卡:" + card.getSerialNumber();

        ExchangeRecord record = ExchangeRecord.builder()
                .uid(uid)
                .category(MONEY_EXCHANGE.PAY_CARD_EXCHANGE)
                .correlationId(card.getId())
                .exchangeMoney(card.getDenomination())
                .balance(balance)
                .comment(comment)
                .build();
        // 记录账户变更信息
        this.erRepository.save(record);

        response.setItem(balance);

        return response;
    }

    // endregion

    // region 附加的记录信息

    /**
     * 管理员获取所有的兑换记录
     * @param index page index
     * @param size page size
     * @return Page of ExchangeRecord
     */
    public PageResponse<ExchangeRecord> exchangeRecordPage(int index, int size) {
        PageResponse<ExchangeRecord> response = new PageResponse<>();
        Page<ExchangeRecord> page = this.erRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("category"), MONEY_EXCHANGE.PAY_CARD_EXCHANGE),
                ZsTool.pageable(index, size)
        );
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }

    /**
     * 用户获取自己的兑换卡兑换记录
     * @param uid user id
     * @param index page index
     * @param size page size
     * @return Page of ExchangeRecord
     */
    public PageResponse<ExchangeRecord> exchangeRecordPage(long uid, int index, int size) {
        PageResponse<ExchangeRecord> response = new PageResponse<>();
        Page<ExchangeRecord> page = this.erRepository.findAll(
                (root, query, cb) -> {
                    Predicate predicate = cb.equal(root.get("uid"), uid);
                    Predicate predicate1 = cb.equal(root.get("category"), MONEY_EXCHANGE.PAY_CARD_EXCHANGE);
                    return cb.and(predicate, predicate1);
                },
                ZsTool.pageable(index, size));
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }

    /**
     * 管理员获取卡片操作记录
     * @param index page index
     * @param size page size
     * @return Page Of ExchangeRecord
     */
    public PageResponse<PayCardLog> payCardLogList(int index, int size) {
        PageResponse<PayCardLog> response = new PageResponse<>();
        Page<PayCardLog> page = this.payCardLogRepository.findAll(ZsTool.pageable(index, size));
        response.setList(page.getContent());
        response.setTotal(page.getTotalElements());
        return response;
    }

    // endregion

    /**
     * 用户获取系统分配给自己的还未使用的卡片
     * @param uid user id
     * @param index page index
     * @param size page size
     * @return Page of PayCard
     */
    public PageResponse<PayCard> customerPayCards(long uid, int index, int size) {
        PageResponse<PayCard> response = new PageResponse<>();
        Page<Object> page = this.payCardRepository
                .findAllCustomerValidCards(uid, 1,0, ZsTool.pageable(index, size));

        List content = page.getContent();
        List<PayCard> payCards = new LinkedList<>();
        content.forEach(x -> {
            Object[] objects = (Object[]) x;
            if (objects[0] instanceof PayCard) {
                payCards.add((PayCard) objects[0]);
            } else {
                throw new RuntimeException("不是兑换卡类型");
            }
        });
        response.setTotal(page.getTotalElements());
        response.setList(payCards);
        return response;
    }

    /**
     * 用户移除自己卡片列表中的对话卡
     * <p>1.卡片过期了</p>
     * <p>2.卡片送人了</p>
     * @param uid user id
     * @param sn card serial number
     * @param vc verify code
     * @return BaseResponse
     */
    @Transactional
    public BaseResponse invalidCard(long uid, String username, String sn, String vc) {
        BaseResponse response = new BaseResponse();
        PayCard payCard = this.payCardRepository.findOneBySerialNumberAndVerifyCode(sn, vc);
        if (payCard == null) {
            response.setStatus(HTTP_CODE.FAILED);
            response.setMessage("对话卡不存在了，请刷新页面");
            return response;
        }
        payCard.setIsValid(0);
        this.payCardRepository.save(payCard);
        this.payCardLogRepository.save(PayCardLog.builder()
                .cardId(payCard.getId())
                .optDescription(PAY_CARD_OPT.INVALID)
                .opt(PAY_CARD_OPT.INVALID_)
                .optId(uid)
                .optUsername(username)
                .build());
        return response;
    }
}
