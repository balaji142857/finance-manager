package com.kb.fm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Asset extends BaseEntity {
	
	@Id
	@GeneratedValue(generator = "seq_asset_id", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "seq_asset_id", sequenceName = "seq_asset_id", allocationSize = 1)
	private Long id;
	@Column(unique =   true)
	private String name;
	@Column(name="user_comment")
	private String comment;
	private BigDecimal usage;
	@OneToMany(mappedBy = "asset")
	@JsonIgnore
	private List<Expense> expenses;
}
