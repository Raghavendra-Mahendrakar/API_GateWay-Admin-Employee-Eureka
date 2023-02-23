package com.pws.employee.entity;

import java.io.Serializable;

import org.hibernate.annotations.ColumnDefault;

import com.pws.employee.utility.AuditModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "permissions")
public class Permission extends AuditModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name="is_active",nullable = false)
	@ColumnDefault("TRUE")
	private Boolean isActive;
	
	@Column(name="is_view",nullable = false)
	@ColumnDefault("TRUE")
	private Boolean isView;
	
	@Column(name="is_add",nullable = false)
	@ColumnDefault("TRUE")
	private Boolean isAdd;
	
	@Column(name="is_update",nullable = false)
	@ColumnDefault("TRUE")
	private Boolean isUpdate;
	
	@Column(name="is_delete",nullable = false)
	@ColumnDefault("TRUE")
	private Boolean isDelete;
	
	@ManyToOne
	@JoinColumn(name="model_id")
	private Module module;
	
	@ManyToOne
	@JoinColumn(name="role_id")
	private Role role;
	
	

}
