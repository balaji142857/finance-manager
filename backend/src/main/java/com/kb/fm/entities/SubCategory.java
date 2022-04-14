package com.kb.fm.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@Table(name="SUBCATEGORY")
public class SubCategory {
	
	@Id
	@GeneratedValue(generator = "seq_subcat_id", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "seq_subcat_id", sequenceName = "seq_subcat_id", allocationSize = 1)
	private Long id;
	private String name;
	@ManyToOne
	@JoinColumn(name="cat_id")
	@JsonIgnore
	private Category category;

}
