package com.kb.fm.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kb.fm.entities.Asset;
import com.kb.fm.service.AssetService;

@RestController
@RequestMapping("/assets")
public class AssetController {
	
	@Autowired
	private AssetService service;
	
	@GetMapping
	public ResponseEntity<Collection<Asset>> listAssets() {
		return ResponseEntity.status(HttpStatus.OK).body(service.listAssets());
	}
	
	@PostMapping
	public ResponseEntity<Asset> save(@RequestBody Asset asset) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(asset));
	}
	
	@PostMapping("/{id}")
	public void deleteAsset(@PathVariable("id") Long assetId) {
		service.deleteAsset(assetId);
	}

}
