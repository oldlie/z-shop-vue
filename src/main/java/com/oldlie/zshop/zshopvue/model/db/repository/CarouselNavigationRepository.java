package com.oldlie.zshop.zshopvue.model.db.repository;

import com.oldlie.zshop.zshopvue.model.db.CarouselNavigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CarouselNavigationRepository extends JpaRepository<CarouselNavigation, Long>,
        JpaSpecificationExecutor<CarouselNavigation> {
}
