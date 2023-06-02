package com.socialmedia.api.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class GenericSpecification<T> implements Specification<T> {
    private final List<SearchCriteria> searchCriteria = new ArrayList<>();
    public void add(SearchCriteria criteria) {
        searchCriteria.add(criteria);
    }

    @Override
    public Predicate toPredicate( Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchCriteria criteria : searchCriteria) {
            Expression<String> expression = buildExpression(criteria.getKey(), root);
            switch (criteria.getOperation()) {
                case GREATER_THAN -> predicates.add(
                        criteriaBuilder.greaterThan(expression,criteria.getValue().toString()));
                case LESS_THAN -> predicates.add(
                        criteriaBuilder.lessThan(expression,criteria.getValue().toString()));
                case GREATER_THAN_EQUAL -> predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(expression,criteria.getValue().toString()));
                case LESS_THAN_EQUAL -> predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(expression,criteria.getValue().toString()));
                case NOT_EQUAL -> predicates.add(
                        criteriaBuilder.notEqual(expression,criteria.getValue()));
                case EQUAL -> predicates.add(
                        criteriaBuilder.equal(expression,criteria.getValue()));
                case IN -> predicates.add(
                        criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));



            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private Expression<String> buildExpression(String key, Path<T> path) {
        if (!key.contains("."))
            return path.get(key);
        String[] parts = key.split("\\.");
        int i = 0;
        for (; i < parts.length - 1; i++) {
            path = path.get(parts[i]);
        }
        return path.get(parts[i]);
    }
}
