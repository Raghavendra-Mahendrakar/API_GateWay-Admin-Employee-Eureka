package com.pws.admin.dto;

import com.pws.admin.entity.Permission;

import lombok.Data;
@Data
public class PermissionDTO {
	
	

	 	private Integer Id;

	    private Boolean isActive;

	    private Boolean isView;

	    private Boolean isAdd;
	    
	    private Boolean isUpdate;
	    
	    private Boolean isDelete;
	    
		private Integer module;
		
		private Integer role;




}
