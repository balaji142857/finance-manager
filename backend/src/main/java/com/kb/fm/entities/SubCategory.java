package com.kb.fm.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
