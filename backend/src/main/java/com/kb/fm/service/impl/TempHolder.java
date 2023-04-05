package com.kb.fm.service.impl;

import com.kb.fm.entities.Expense;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempHolder {
	
	CriteriaBuilder cb;
	CriteriaQuery<?> query;
	Root<Expense> root;

}
