package com.kb.fm.web;

import com.kb.fm.entities.Asset;
import com.kb.fm.service.AssetService;
import com.kb.fm.web.model.AssetModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assets")
public class AssetController {
	
	private final AssetService service;
	
	@GetMapping
	public ResponseEntity<Collection<AssetModel>> listAssets() {
		Collection<Asset> assets = service.listAssets();
		List<AssetModel> list = CollectionUtils.isEmpty(assets) ? List.of() : assets.stream().map(this::entityToModel).toList();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@PostMapping
	public ResponseEntity<AssetModel> save(@RequestBody AssetModel asset) {
		return ResponseEntity.status(HttpStatus.CREATED).body(entityToModel(service.save(modelToEntity(asset))));
	}
	
	@PostMapping("/{id}")
	public void deleteAsset(@PathVariable("id") Long assetId) {
		service.deleteAsset(assetId);
	}

	private Asset modelToEntity(AssetModel model) {
		Asset asset = new Asset();
		// INFO expenses not mapped
		BeanUtils.copyProperties(model,asset,"expenses");
		return asset;
	}

	private AssetModel entityToModel(Asset asset) {
		AssetModel model = new AssetModel();
		BeanUtils.copyProperties(asset, model, "expenses");
		// INFO expenses not mapped
		return model;
	}

}
