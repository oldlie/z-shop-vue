package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author CL
 * @date 2020/5/25
 */
public interface WalletRepository extends JpaRepository<Wallet, Long>, JpaSpecificationExecutor<Wallet> {

    /**
     * find one by user id
     * @param uid user id
     * @return wallet information
     */
    Wallet findOneByUid(long uid);

    /**
     * 查找到越
     * @return
     */
//    @Query("FROM Wallet w join User u on w.uid=u.id")
//    Page<Object> allUserAndWallet();
}
