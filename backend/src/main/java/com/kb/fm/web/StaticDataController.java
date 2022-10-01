package com.kb.fm.web;

import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kb.fm.config.ImportConfig;
import com.kb.fm.web.model.imports.ColumnModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/static/")
@RequiredArgsConstructor
@Slf4j
public class StaticDataController {
	
	private final ImportConfig importConfig;

	@GetMapping("import-formats")
	public Set<String> getAllowedFormats() {
		Map<String, Map<String, ColumnModel>> allowedFormats = importConfig.getImportFormats();
		log.info("formats are {}", allowedFormats);
		return allowedFormats.keySet();
	}
}
