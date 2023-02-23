package com.pws.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pws.admin.entity.Skill;


@Repository

public interface SkillRepository extends JpaRepository<Skill, Integer> {

}
