package com.pws.admin.repository;

import com.pws.admin.entity.Role;
import com.pws.admin.entity.User;
import com.pws.admin.entity.UserRoleXref;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author Vinayak M
 * @Date 10/01/23
 */

@Repository
public interface UserRoleXrefRepository extends JpaRepository<UserRoleXref, Integer> {

    @Query("select o.user from UserRoleXref o where o.role.id= :roleId")
    List<User> fetchAllUsersByRoleId(Integer roleId);
    

    @Query("select o.role from UserRoleXref o where o.user.id= :id and o.role.IsActive=TRUE")
    List<Role> findAllUserRoleByUserId(Integer id);
    
}
