package com.kb.fm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.fm.entities.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {
	
	Category findByName(String name);

}
