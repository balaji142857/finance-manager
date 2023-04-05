package com.kb.fm.service.impl;

import com.kb.fm.entities.Category;
import com.kb.fm.repo.CategoriesRepository;
import com.kb.fm.service.ExpCatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpCatServiceImpl implements ExpCatService {
	
	private final CategoriesRepository repo;
	
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<Category> listCategories() {
		return repo.findAll();
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<Category> listCategoriesById(Iterable<Long> ids) {
		return repo.findAllById(ids);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Category saveCategory(Category cat) {
		return repo.save(cat);
	}

}
