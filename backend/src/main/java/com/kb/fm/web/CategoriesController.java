package com.kb.fm.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kb.fm.entities.Category;
import com.kb.fm.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public List<Category> categories() {
		return service.getCategories();
	}
	
	@PostMapping
	public Category save(@RequestBody Category cat) {
		return service.save(cat);
	}
	
	@PostMapping("delete/{id}")
	public void delete(@PathVariable Long id) {
		service.deleteCategory(id);
	}
}