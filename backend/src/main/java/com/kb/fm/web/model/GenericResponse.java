package com.kb.fm.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
	
	private T data;
	private String message;

	private List<String> errorMessages;

}
