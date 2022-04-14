package com.kb.fm.service;

import java.util.List;

import com.kb.fm.entities.Category;
import com.kb.fm.entities.SubCategory;

public interface CategoryService {

	List<Category> getCategories();

	Category save(Category cat);
	
	void deleteCategory(Long id);
	
	Category findCategory(Long id);
	
	Category findCategory(String name);
	
	SubCategory findSubCategory(String name);
	
	SubCategory findSubCategory(Long id);

}
