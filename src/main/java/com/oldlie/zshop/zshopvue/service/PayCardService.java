package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.cs.MONEY_EXCHANGE;
import com.oldlie.zshop.zshopvue.model.db.ExchangeRecord;
import com.oldlie.zshop.zshopvue.model.db.PayCard;
import com.oldlie.zshop.zshopvue.model.db.PayCardLog;
import com.oldlie.zshop.zshopvue.model.db.Wallet;
import com.oldlie.zshop.zshopvue.model.db.repository.ExchangeRecordRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.PayCardLogRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.PayCardRepository;
import com.oldlie.zshop.zshopvue.model.db.repository.WalletRepository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.PageResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import com.oldlie.zshop.zshopvue.utils.ZsTool;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private WalletRepository walletRepository;

    @Autowired
    public void setPayCardRepository(PayCardRepository payCardRepository) {
        this.payCardRepository = payCardRepository;
    }

    @Autowired
    public void setPayCardLogRepository(PayCardLogRepository payCardLogRepository) {
        this.payCardLogRepository = payCardLogRepository;
    }

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
        log.setOptDescription("CREATE");
        log.setOpt(1);
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
            log.setOpt(2);
            log.setOptDescription("DELETE");
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

    @Transactional
    public BaseResponse sold(final String idStr,
                             final String customer,
                             final String customerPhone,
                             final String amount,
                             final long uid,
                             final String username) {
        BaseResponse response = new BaseResponse();
        String[] ids = idStr.split(",");
        for (String _id : ids) {
            long id = Long.parseLong(_id);
            this.payCardRepository.findById(id).ifPresent(x -> {
                x.setCustomer(customer);
                x.setCustomerPhone(customerPhone);
                x.setPrice(Money.parse(amount));
                x.setIsSoldOut(1);
                this.payCardRepository.save(x);
            });
            this.payCardLogRepository.save(
                    PayCardLog.builder()
                            .cardId(id)
                            .optDescription("SOLD")
                            .optUsername(username)
                            .optId(uid)
                            .opt(2)
                            .build()
            );
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
                .opt(2)
                .optDescription("USER EXCHANGE")
                .build();
        // 记录卡片变更概要信息
        this.payCardLogRepository.save(log);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String comment = format.format(new Date())
                +  " 兑换卡充值:"+ card.getDenomination().getAmountMajorLong()
                + " 兑换卡:" + card.getSerialNumber();

        ExchangeRecord record = ExchangeRecord.builder()
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
}
