package com.kb.fm.service;

import java.util.Collection;

import com.kb.fm.entities.Asset;

public interface AssetService {

	Collection<Asset> listAssets();

	Asset save(Asset asset);
	
	Asset getAsset(Long id);
	
	void deleteAsset(Long id);
	
	Asset getAsset(String name);

}
