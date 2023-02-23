package com.pws.admin.entity;

import java.io.Serializable;


import org.hibernate.annotations.ColumnDefault;

import com.pws.admin.utility.AuditModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "module")
public class Module extends AuditModel implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name="is_active")
	@ColumnDefault("TRUE")
	private Boolean isActive;
	
	@Column(name="name", nullable = false, unique = true)
	private String name;

}
