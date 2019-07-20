package com.oldlie.zshop.zshopvue.service;

import com.oldlie.zshop.zshopvue.model.AppRequest;
import com.oldlie.zshop.zshopvue.model.cs.HTTP_CODE;
import com.oldlie.zshop.zshopvue.model.db.Address;
import com.oldlie.zshop.zshopvue.model.db.repository.AddressRespository;
import com.oldlie.zshop.zshopvue.model.response.BaseResponse;
import com.oldlie.zshop.zshopvue.model.response.ListResponse;
import com.oldlie.zshop.zshopvue.model.response.SimpleResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressService {

    private AddressRespository addressRespository;

    @Autowired
    public void setAddressRespository(AddressRespository addressRespository) {
        this.addressRespository = addressRespository;
    }

    public SimpleResponse<Long> store(AppRequest<Address> request, Long uid) {
        SimpleResponse<Long> response = new SimpleResponse<>();
        Address address = request.getBody();
        Address target = null;
        if (address.getId() > 0) {
            target = this.addressRespository.findById(address.getId()).orElseGet(null);
            if (target == null) {
                response.setStatus(HTTP_CODE.FAILED);
                response.setMessage("要修改的地址不存在了。");
                return response;
            }
        } else {
            target = new Address();
        }
        target.setAddress(address.getAddress());
        target.setCity(address.getCity());
        target.setContactName(address.getContactName());
        target.setContactPhone(address.getContactPhone());
        target.setCounty(address.getCounty());
        target.setIsDefault(address.getIsDefault());
        target.setProvince(address.getProvince());
        long userId = address.getUid() <= 0 ? uid : address.getUid();
        target.setUid(userId);
        target.setInfo(this.formatAddress(target));
        target = this.addressRespository.save(target);
        response.setItem(target.getId());

        return response;
    }

    public BaseResponse delete(AppRequest<Long> request) {
        BaseResponse response = new BaseResponse();
        Long id = request.getBody();
        this.addressRespository.deleteById(id);
        return response;
    }

    public ListResponse<Address> listAddress(Long uid) {
        ListResponse<Address> response = new ListResponse<>();
        List<Address> list = this.addressRespository.findAllByUidOrderByIdDesc(uid);
        response.setList(list);
        return response;
    }

    public String formatAddress(Long id) {
        Address address = this.addressRespository.findById(id).orElse(null);
        if (address == null) {
            return null;
        }
        return this.formatAddress(address);
    }

    public String formatAddress(Address address) {
        StringBuilder builder = new StringBuilder(128);
        if (StringUtils.isNotEmpty(address.getContactName())) {
            builder.append(address.getContactName()).append(" ");
        }
        if (StringUtils.isNotEmpty(address.getContactPhone())) {
            builder.append(address.getContactPhone()).append(" ");
        }
        if (StringUtils.isNotEmpty(address.getProvince())) {
            builder.append(address.getProvince()).append(" ");
        }
        if (StringUtils.isNotEmpty(address.getCity())) {
            builder.append(address.getCity()).append(" ");
        }
        if (StringUtils.isNotEmpty(address.getCounty())) {
            builder.append(address.getCounty()).append(" ");
        }
        if (StringUtils.isNotEmpty(address.getAddress())) {
            builder.append(address.getAddress());
        }
        return builder.toString();
    }

}
