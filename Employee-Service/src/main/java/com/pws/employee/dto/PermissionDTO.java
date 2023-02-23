package com.pws.employee.dto;

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
