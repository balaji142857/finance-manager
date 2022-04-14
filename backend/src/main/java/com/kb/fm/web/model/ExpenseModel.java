package com.kb.fm.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseModel {

	private Long id;
	private Long category;
	private Long subCategory;
	private String comment;
//	private List<Tag> tags;
	private Long asset;
	private Double amount;
	private String transactionDate;
	private String transactionDetail;
	private boolean isReviewRequired;
	private String bankFormat;
	
}
