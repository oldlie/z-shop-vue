package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.PayCard;
import com.oldlie.zshop.zshopvue.model.db.PayCardLog;
import com.oldlie.zshop.zshopvue.model.db.repository.PayCardLogRespository;
import com.oldlie.zshop.zshopvue.model.db.repository.PayCardRepository;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class PayCardService {

    private PayCardRepository payCardRepository;
    private PayCardLogRespository payCardLogRespository;

    @Autowired
    public void setPayCardRepository(PayCardRepository payCardRepository) {
        this.payCardRepository = payCardRepository;
    }

    @Autowired
    public void setPayCardLogRespository(PayCardLogRespository payCardLogRespository) {
        this.payCardLogRespository = payCardLogRespository;
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
        this.payCardLogRespository.save(log);

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
            this.payCardLogRespository.save(
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
}
