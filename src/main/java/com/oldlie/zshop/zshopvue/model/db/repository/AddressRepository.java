package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author oldlie
 */
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    /**
     * Find all by user id order by id desc
     * @param uid user id
     * @return address list
     */
    List<Address> findAllByUidOrderByIdDesc(Long uid);

    /**
     * Find one by user id and is default
     * @param uid user id
     * @param isDefault is default
     * @return address
     */
    Address findOneByUidAndIsDefault(long uid, int isDefault);

    /**
     * Delete by id and user id
     * @param id id
     * @param uid user id
     */
    void deleteByIdAndUid(long id, long uid);
}
