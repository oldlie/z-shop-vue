package com.oldlie.zshop.zshopvue.model.db;

import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Date;

public class SpecificationFactory<T> {

    public Specification<T> containsLike(String attribute, String value) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get(attribute), "%" + value + "%");
    }

    public Specification<T> equal(String attribute, Object value) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(attribute), value);
    }

    public Specification<T> isBetween(String attribute, int min, int max) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get(attribute), min, max);
    }

    public Specification<T> isBetween(String attribute, double min, double max) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get(attribute), min, max);
    }

    public Specification<T> isBetween(String attribute, Date min, Date max) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get(attribute), min, max);
    }

    public Specification<T> in(String attribute, Collection c) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(attribute).in(c);
    }
}
