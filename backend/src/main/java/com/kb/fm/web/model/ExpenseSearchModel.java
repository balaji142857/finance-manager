package com.kb.fm.web.model;

import java.util.List;

import javax.persistence.criteria.Join;

import com.kb.fm.entities.Asset;
import com.kb.fm.entities.Category;
import com.kb.fm.entities.Expense;

import lombok.Data;

@Data
public class ExpenseSearchModel {
	private List<Long> asset;
	private List<Long> category;
	private List<Long> subCategory;
	private String fromDate;
	private String toDate;
	private Double minAmount;
	private Double maxAmount;
	private String txDetail;
	private String comment;
	
	private String chartType;
	private Join<Expense, Category> catJoin;
	private Join<Expense, Asset> assetJoin;
}
