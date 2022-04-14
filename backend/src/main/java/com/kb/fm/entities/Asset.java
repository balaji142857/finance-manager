package com.kb.fm.entities;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
