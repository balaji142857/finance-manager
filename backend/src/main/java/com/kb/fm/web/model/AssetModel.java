package com.kb.fm.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetModel {

    private Long id;
    private String name;
    private String comment;
    private BigDecimal usage;
    private List<ExpenseModel> expenses;
}
