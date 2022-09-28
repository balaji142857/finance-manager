package com.kb.fm.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.kb.fm.web.model.imports.ColumnModel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//TODO: move to db
@ConfigurationProperties(prefix="bulk-imports", ignoreUnknownFields = false)
@Getter
@Setter
@RequiredArgsConstructor
public class ImportConfig {

	private Map<String, Map<String, ColumnModel>> importFormats;
	
}
