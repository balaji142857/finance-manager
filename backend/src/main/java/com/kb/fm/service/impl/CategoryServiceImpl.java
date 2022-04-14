package com.kb.fm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.kb.fm.entities.Category;
import com.kb.fm.entities.SubCategory;
import com.kb.fm.repo.CategoriesRepository;
import com.kb.fm.repo.SubCategoryRepository;
import com.kb.fm.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoriesRepository repo;
	
	@Autowired
	private SubCategoryRepository subCatRepo;
	
	@Transactional
	@Override
	public List<Category> getCategories() {
		return repo.findAll();
	}

	@Override
	@Transactional(rollbackFor =  Exception.class)
	public Category save(Category cat) {
		Category dbCat = null == cat.getId() ? new Category() :repo.getOne(cat.getId());  
		dbCat.setName(cat.getName());
		if (!CollectionUtils.isEmpty(cat.getSubCategories())) {
			cat.getSubCategories().stream().forEach(subCat -> {
				subCat.setCategory(dbCat);
			});
		}
		if (CollectionUtils.isEmpty(dbCat.getSubCategories())) {
			dbCat.setSubCategories(new ArrayList<>());
		}
		dbCat.getSubCategories().clear();
		dbCat.getSubCategories().addAll(cat.getSubCategories());
		repo.save(dbCat);
		return dbCat;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Category findCategory(Long id) {
		Optional<Category> optional = repo.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public SubCategory findSubCategory(Long id) {
		Optional<SubCategory> optional = subCatRepo.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Category findCategory(String name) {
		return repo.findByName(name);
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public SubCategory findSubCategory(String name) {
		return subCatRepo.findByName(name);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteCategory(Long id) {
		repo.deleteById(id);
	}
	
}
