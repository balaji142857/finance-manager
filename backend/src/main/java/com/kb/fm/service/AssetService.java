package com.kb.fm.service;

import com.kb.fm.entities.Asset;

import java.util.Collection;

public interface AssetService {

	Collection<Asset> listAssets();

	Collection<Asset> listAssetsById(Iterable<Long> ids);

	Asset save(Asset asset);

	Asset getAsset(Long id);
	
	void deleteAsset(Long id);
	
	Asset getAsset(String name);

}
