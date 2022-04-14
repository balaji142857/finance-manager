package com.kb.fm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kb.fm.entities.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
	
	public Asset findByName(String name);

}
