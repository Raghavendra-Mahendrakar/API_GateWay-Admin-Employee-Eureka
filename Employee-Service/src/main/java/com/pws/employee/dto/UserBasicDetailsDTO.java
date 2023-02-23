package com.pws.employee.dto;



import java.util.List;

import com.pws.employee.entity.Permission;
import com.pws.employee.entity.Role;
import com.pws.employee.entity.Skill;
import com.pws.employee.entity.User;
import com.pws.employee.entity.UserSkillXref;

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

    private List<Skill> skilllist;
    
    

   
}
