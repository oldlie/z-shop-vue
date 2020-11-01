package com.oldlie.zshop.zshopvue.model.db.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author oldlie
 * @date 2020/11/1
 */
public class KeyValueSpecification<T> implements Specification<T> {

    private final String key;
    private final Object value;

    public KeyValueSpecification(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.equal(root.get(this.key), this.value);
    }

    public static <T> KeyValueSpecification<T> getInstance(String key, Object value) {
        return new KeyValueSpecification<>(key, value);
    }
}
