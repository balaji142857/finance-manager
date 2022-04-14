package com.kb.fm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kb.fm.entities.Category;
import com.kb.fm.repo.CategoriesRepository;
import com.kb.fm.service.ExpCatService;

@Service
public class ExpCatServiceImpl implements ExpCatService {
	
	@Autowired
	private CategoriesRepository repo;
	
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<Category> listCategories() {
		return repo.findAll();
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Category saveCategory(Category cat) {
		return repo.save(cat);
	}

}
