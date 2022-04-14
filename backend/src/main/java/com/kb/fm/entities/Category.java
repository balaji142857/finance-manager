package com.kb.fm.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {
	
	@Id
	@GeneratedValue(generator = "seq_cat_id", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "seq_cat_id", sequenceName = "seq_cat_id", allocationSize = 1)
	private Long id;
	@Column(unique = true)
	private String name;
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SubCategory> subCategories;
	

}
