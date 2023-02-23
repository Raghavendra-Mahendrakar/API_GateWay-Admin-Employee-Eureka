package com.pws.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pws.employee.entity.Module;

/**
 * @Author Vinayak M
 * @Date 10/01/23
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
}
