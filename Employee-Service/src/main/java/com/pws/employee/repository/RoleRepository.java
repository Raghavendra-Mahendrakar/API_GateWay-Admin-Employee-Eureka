package com.pws.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pws.employee.entity.Role;
import com.pws.employee.entity.User;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	@Query("select o from Role o where o.name = :rolename")
    Optional<Role> findByRolename(String rolename);

}
