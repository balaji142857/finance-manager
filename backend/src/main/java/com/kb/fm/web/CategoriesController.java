package com.kb.fm.web;

import com.kb.fm.entities.Category;
import com.kb.fm.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {
	
	private final CategoryService service;
	
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