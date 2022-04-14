package com.kb.fm.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name="EXP")
@EqualsAndHashCode(callSuper = true)
public class Expense extends BaseEntity{
	
	@Id
	@GeneratedValue(generator = "seq_exp_id", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "seq_exp_id", sequenceName = "seq_exp_id", allocationSize = 1)
	private Long id;
	@OneToOne
	@JoinColumn(name="cat_id")
	private Category category;
	@OneToOne
	@JoinColumn(name="subcat_id")
	private SubCategory subCategory;
	@Column(name="user_comment")
	private String comment;
	@ManyToMany
	@JoinTable(name="TAG_EXP", inverseJoinColumns = @JoinColumn(name="tag_id"),
	joinColumns = @JoinColumn(name="exp_id"))
	private List<Tag> tags;
	@OneToOne
	@JoinColumn(name = "asset_id")
	private Asset asset;
	private BigDecimal amount;
	@Column(name="tx_date")
	private Date transactionDate;
	@Column(name="transaction_detail")
	private String transactionDetail;
//	@OneToOne
//	private Document document;

}
