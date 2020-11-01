package com.oldlie.zshop.zshopvue.model.db.specification;

import com.oldlie.zshop.zshopvue.model.db.BaseEo;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class IdSpecification<T, ID> implements Specification<T> {

    private ID id;

    public IdSpecification(ID id) {
        this.id = id;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(BaseEo.ID), this.id);
    }

    public static <T, ID> IdSpecification<T, ID> getInstance(ID id) {
        return new IdSpecification<>(id);
    }
}
