package com.kb.fm.entities;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public abstract class BaseEntity {
	
	@CreatedDate
	private Date created;
	@LastModifiedDate
	private Date lastModified;

}
