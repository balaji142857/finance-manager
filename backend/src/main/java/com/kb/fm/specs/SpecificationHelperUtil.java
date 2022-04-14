package com.kb.fm.specs;

import static com.kb.fm.util.DateUtil.convert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.CollectionUtils;


import lombok.experimental.UtilityClass;

@UtilityClass
public class SpecificationHelperUtil {

	public static final String LIKE = "%";

	public static <T, V extends Comparable<V>> Predicate addConditionForNumberRange(Root<T> root, CriteriaBuilder cb, String property, V from, V to) {
		if (null == from && null == to) {
			return null;
		} else if (null != from && null != to) {
			return cb.between(root.get(property), from, to);
		}
		else if (null != from) {
			return cb.greaterThanOrEqualTo(root.get(property), from);
		}
		else {
			return cb.lessThanOrEqualTo(root.get(property), to);
		}
	}

	public static <T> Predicate addConditionsForDateRage(Root<T> root, CriteriaBuilder cb, String property,
			String fromDate, String toDate) {
		if (null == fromDate && null == toDate) {
			return null;
		}
		else if (null != fromDate && null != toDate) {
			return cb.between(root.get(property), convert(fromDate), convert(toDate));
		}
		else if(null != fromDate) {
			return cb.greaterThanOrEqualTo(root.get(property), convert(fromDate));
		} else {
			return cb.lessThanOrEqualTo(root.get(property), convert(toDate));
		}
	}

	public static Predicate getPredicate(CriteriaBuilder cb, List<Predicate> predicates) {
		if (CollectionUtils.isEmpty(predicates)) {
			predicates.add(cb.conjunction());
		}
		List<Predicate> validPredicates = predicates.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (validPredicates.isEmpty())
			return cb.conjunction();
		if (validPredicates.size() == 1)
			return validPredicates.get(0);
		Predicate previous = validPredicates.get(0), current, finalPredicate = null;
		for (int i = 1; i < validPredicates.size(); i++) {
			current = validPredicates.get(i);
			finalPredicate = cb.and(previous, current);
			previous = current;
		}
		return finalPredicate;
	}
	
	public void addCondition(CriteriaQuery<?> query, Predicate addConditionForNumberRange) {
		if (null != addConditionForNumberRange) {
			query.where(addConditionForNumberRange);
		}
	}

	public static String getContains(String input) {
		return LIKE + input + LIKE;
	}

	public static String getStartsWith(String input) {
		return input + LIKE;
	}

	public static String getEndsWith(String input) {
		return LIKE + input;
	}
}
