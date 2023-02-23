package com.pws.admin.dto;



import java.util.List;

import com.pws.admin.entity.Permission;
import com.pws.admin.entity.Role;
import com.pws.admin.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicDetailsDTO {
	

	private User user;

    private List<Role> roleList;


    private List<Permission> permissionList;


   
}
