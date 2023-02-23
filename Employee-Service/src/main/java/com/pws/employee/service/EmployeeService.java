package com.pws.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pws.employee.dto.SignUpDTO;
import com.pws.employee.dto.UpdatePasswordDTO;
import com.pws.employee.dto.UserBasicDetailsDTO;
import com.pws.employee.dto.UserSkillXrefDTO;
import com.pws.employee.entity.Skill;
import com.pws.employee.entity.UserSkillXref;
import com.pws.employee.exception.config.PWSException;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
public interface EmployeeService {

    void UserSignUp(SignUpDTO signupDTO) throws PWSException;

    //Update Password Service
  	
    void updateUserPassword(UpdatePasswordDTO userPasswordDTO)throws PWSException;
    
    UserBasicDetailsDTO getUserBasicInfoAfterLoginSuccess(String  email) throws PWSException;


    //Skill Services


    public Page<Skill> fetchAllSkills(int offset,int pageSize, String field) throws PWSException;

	
	void addSkillToUser(UserSkillXrefDTO userSkillXrefDTO) throws PWSException;

	Optional<Skill> fetchAllSkillsByid(Integer id) throws PWSException;

	void saveOrUpdateskilluserXref(UserSkillXrefDTO userSkillXrefDTO) throws PWSException;

	List<Skill> fetchAllActiveSkills() throws PWSException;

	List<Skill> fetchAllSkillsByFlag(Boolean flag) throws PWSException;
	
	public Page<Skill> fetchAllUserSkills(int page,int pageSize, String sort, String order, Integer id)throws PWSException;
	


	void deactivateOrActivateSkillUserXref(Integer id, Boolean flag) throws PWSException;


//    void updateRole(Role role) throws PWSException;
//
//    List<Role> fetchAllRole() throws PWSException;

   


	


}
