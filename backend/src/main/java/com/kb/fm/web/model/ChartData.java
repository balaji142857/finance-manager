package com.kb.fm.web.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ChartData {
	
	@NonNull
	private String label;
	@NonNull
	private BigDecimal value;
	private Object temp;
	
}
