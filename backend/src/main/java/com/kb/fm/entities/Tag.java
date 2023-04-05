package com.kb.fm.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
