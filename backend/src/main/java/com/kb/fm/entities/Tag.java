package com.kb.fm.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Tag extends BaseEntity {
	
	@Id
	@GeneratedValue(generator = "seq_tag_id", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "seq_tag_id", sequenceName = "seq_tag_id", allocationSize = 1)
	private Long id;
	private String name;
	@ManyToMany
	@JoinTable(name="TAG_EXP",joinColumns = @JoinColumn(name="tag_id"),
	inverseJoinColumns = @JoinColumn(name="exp_id"))
	private List<Expense> expenses;

}
