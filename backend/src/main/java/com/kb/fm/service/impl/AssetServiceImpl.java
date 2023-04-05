package com.kb.fm.service.impl;

import com.kb.fm.entities.Asset;
import com.kb.fm.exceptions.FinanceManagerException;
import com.kb.fm.repo.AssetRepository;
import com.kb.fm.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

	private final AssetRepository repo;

	@Override
	public List<Asset> listAssetsById(Iterable<Long> ids) {
		return repo.findAllById(ids).stream().toList();
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = true)	
	public Collection<Asset> listAssets() {
		return getAssetMap().values();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Asset save(Asset asset) {
		Objects.requireNonNull(asset);

		Asset dbEntity;
		if (null == asset.getId()) {
			dbEntity = new Asset();
			dbEntity.setUsage(new BigDecimal(0));
		} else {
			dbEntity = repo.getReferenceById(asset.getId());
		}
		dbEntity.setComment(asset.getComment());
		dbEntity.setName(asset.getName());
		dbEntity.setUsage(asset.getUsage());
		if (CollectionUtils.isEmpty(dbEntity.getExpenses())) {
			dbEntity.setExpenses(new ArrayList<>());
		}
		return repo.save(dbEntity);
	}

	@Override
	public Asset getAsset(Long id) {
		return getAssetMap().get(id);
	}
	
	@Override
	public Asset getAsset(String name) {
		Optional<Asset> optional = getAssetMap().values().stream().filter(a -> a.getName().equals(name)).findFirst();
		return optional.orElse(null);
		
	}

	@Override
	public void deleteAsset(Long id) {
		Asset asset = repo.getReferenceById(id);
		if (CollectionUtils.isEmpty(asset.getExpenses())) {
			repo.deleteById(id);
		} else {
			throw new FinanceManagerException("Cannot remove Asset with expenses associated with it");
		}
	}

	private Map<Long, Asset> getAssetMap() {
		List<Asset> assets = repo.findAll();
		if (CollectionUtils.isEmpty(assets)) {
			return Collections.emptyMap();
		}
		return assets.stream().collect(Collectors.toMap(Asset::getId, Function.identity()));
	}

}
