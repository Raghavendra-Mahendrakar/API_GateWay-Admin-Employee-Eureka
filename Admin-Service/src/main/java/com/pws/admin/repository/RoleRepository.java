package com.pws.admin.repository;

import com.pws.admin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
