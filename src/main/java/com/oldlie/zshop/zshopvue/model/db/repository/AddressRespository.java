package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRespository extends JpaRepository<Address, Long> {
    List<Address> findAllByUidOrderByIdDesc(Long uid);
}
