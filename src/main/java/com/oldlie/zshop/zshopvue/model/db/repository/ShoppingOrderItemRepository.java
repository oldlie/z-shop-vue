package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.ShoppingOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingOrderItemRepository extends JpaRepository<ShoppingOrderItem, Long> {
}
