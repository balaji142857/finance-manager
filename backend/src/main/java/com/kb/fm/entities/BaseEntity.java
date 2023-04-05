package com.kb.fm.entities;

import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {
	
	@CreatedDate
	private Date created;
	@LastModifiedDate
	private Date lastModified;

}
