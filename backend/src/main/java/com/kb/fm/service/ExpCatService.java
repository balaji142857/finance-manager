package com.kb.fm.service;

import java.util.List;

import com.kb.fm.entities.Category;

public interface ExpCatService {

	List<Category> listCategories();

	Category saveCategory(Category cat);

}
