package com.pws.employee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pws.employee.entity.Skill;


@Repository

public interface SkillRepository extends JpaRepository<Skill, Integer> {


}
