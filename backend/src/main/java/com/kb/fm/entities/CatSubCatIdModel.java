package com.kb.fm.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CatSubCatIdModel {

	private Long catId;
	private Long subcatId;

	public CatSubCatIdModel setCatId(Long catId) {
		this.catId = catId;
		return this;
	}

	

	public CatSubCatIdModel setSubcatId(Long subcatId) {
		this.subcatId = subcatId;
		return this;
	}

}
