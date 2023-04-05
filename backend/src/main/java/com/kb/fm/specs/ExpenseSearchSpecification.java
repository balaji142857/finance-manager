package com.kb.fm.specs;


import com.kb.fm.entities.Expense;
import com.kb.fm.web.model.ExpenseSearchModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.kb.fm.specs.SpecificationHelperUtil.*;

@UtilityClass
public class ExpenseSearchSpecification {

	public static final String EMPTY = "";

	public static Specification<Expense> search(ExpenseSearchModel expense) {
		return (root, query, cb) -> buildFilterCriteria(root, cb, expense);
	}
	
	private static Predicate buildFilterCriteria(Root<Expense> root, CriteriaBuilder cb, ExpenseSearchModel model) {
		List<Predicate> predicates = new ArrayList<>();
		if (null != model.getComment()) {
			predicates.add(addNameCheck(root, cb, "comment", model.getComment()));
		}
		if (null != model.getTxDetail()) {
			predicates.add(cb.or(addNameCheck(root, cb, "transactionDetail", model.getTxDetail())));
		}
		if (CollectionUtils.isNotEmpty(model.getAsset())) {
			predicates.add(cb.and((root.get("asset").in(model.getAsset()))));
		}
		if (CollectionUtils.isNotEmpty(model.getCategory())) {
			predicates.add(cb.and(root.get("category").in(model.getCategory())));
		}
		if (CollectionUtils.isNotEmpty(model.getSubCategory())) {
			predicates.add(cb.and(root.get("subCategory").in(model.getSubCategory())));
		}		
		Predicate datePredicate = addConditionsForDateRage(root, cb, "transactionDate", model.getFromDate(), model.getToDate());
		if (null != datePredicate) {
			predicates.add(cb.and(datePredicate));
		}
		Predicate numberPredicate = addConditionForNumberRange(root, cb, "amount", model.getMinAmount(), model.getMaxAmount());
		if (null != numberPredicate) {
			predicates.add(cb.and(numberPredicate));
		}
		return getPredicate(cb, predicates);
	}

	private static Predicate addNameCheck(Root<Expense> root, CriteriaBuilder cb, String attr, String name) {
		return cb.and(cb.like(cb.lower(root.get(attr)), null != name ? getContains(name.toLowerCase()) : EMPTY));
	}

}
