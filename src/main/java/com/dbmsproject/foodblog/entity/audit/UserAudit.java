package com.dbmsproject.foodblog.entity.audit;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@JsonIgnoreProperties(
		value = { "createdBy", "updatedBy" },
		allowGetters = true
)
public abstract class UserAudit extends DateAudit {

    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(updatable = false)
    private int createdBy;

    @LastModifiedBy
    private int updatedBy;
}
