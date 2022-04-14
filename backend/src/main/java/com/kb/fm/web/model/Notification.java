package com.kb.fm.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

	private Long count;
	
	public void increment() {
		this.count++;
	}
}
