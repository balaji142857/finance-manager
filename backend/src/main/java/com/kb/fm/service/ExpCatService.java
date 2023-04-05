package com.kb.fm.service;

import com.kb.fm.entities.Category;

import java.util.List;

public interface ExpCatService {

	List<Category> listCategories();

	List<Category> listCategoriesById(Iterable<Long> ids);

	Category saveCategory(Category cat);

}
