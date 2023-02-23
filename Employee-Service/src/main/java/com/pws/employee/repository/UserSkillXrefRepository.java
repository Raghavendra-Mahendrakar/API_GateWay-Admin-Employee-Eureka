package com.pws.employee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pws.employee.entity.Skill;
import com.pws.employee.entity.UserSkillXref;
import com.pws.employee.exception.config.PWSException;

public interface UserSkillXrefRepository extends JpaRepository<UserSkillXref, Integer> {
   
	
	@Query("select o.skill from UserSkillXref o where o.skill.IsActive=TRUE")
    List<Skill> fetchAllActiveSkills();
	
	@Query("select o.skill from UserSkillXref o where o.skill.IsActive= :flag")
    List<Skill> fetchAllSkillsByFlag(Boolean flag);


	@Query("select o.skill from UserSkillXref o where o.user.id= :id")
	List<Skill> fetchuserSkillsByid(int id);
	
	@Query("select o.skill from UserSkillXref o where o.user.id= :id ")
	Page<Skill> fetchAllUserSkills(Pageable pageable, Integer id);



}
