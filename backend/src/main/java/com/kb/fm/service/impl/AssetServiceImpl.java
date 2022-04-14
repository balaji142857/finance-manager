package com.kb.fm.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.kb.fm.entities.Asset;
import com.kb.fm.repo.AssetRepository;
import com.kb.fm.service.AssetService;

import lombok.RequiredArgsConstructor;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	private AssetRepository repo;

	/**
	 * multiple issues... will lead to issues when deploying in multiple nodes
	 * need to autowire self... not an issue per say but forces test cases to use spy
	 * @return
	 */
	@Cacheable("assets")
	public Map<Long, Asset> assetCache() {
		List<Asset> assets = repo.findAll();
		if (CollectionUtils.isEmpty(assets)) {
			return Collections.emptyMap();
		}
		return assets.stream().collect(Collectors.toMap(Asset::getId, Function.identity()));
	}
	

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)	
	public Collection<Asset> listAssets() {
		return assetCache().values();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Asset save(Asset asset) {
		Asset dbEntity = null;
		if (null == asset.getId()) {
			dbEntity = new Asset();
			dbEntity.setUsage(new BigDecimal(0));
		} else {
			dbEntity = repo.getById(asset.getId());
		}
		dbEntity.setComment(asset.getComment());
		dbEntity.setName(asset.getName());
		dbEntity.setUsage(asset.getUsage());
		if (CollectionUtils.isEmpty(dbEntity.getExpenses())) {
			dbEntity.setExpenses(new ArrayList<>());
		}
		//TODO:BALAJI clear the cache
		return repo.save(dbEntity);
	}

	@Override
	public Asset getAsset(Long id) {
		return assetCache().get(id);
	}
	
	@Override
	public Asset getAsset(String name) {
		Optional<Asset> optional = assetCache().values().stream().filter(a -> a.getName().equals(name)).findFirst();
		//TODO:BALAJI try returning optional itself
		return optional.isPresent() ? optional.get() : null;
		
	}

	@Override
	public void deleteAsset(Long id) {
		Asset asset = repo.getOne(id);
		if (CollectionUtils.isEmpty(asset.getExpenses())) {
			//TOOD:BALAJI update cache
			repo.deleteById(id);
		} else {
			throw new RuntimeException("Cannot remove Asset with expenses associated with it");
		}
		
	}

}
