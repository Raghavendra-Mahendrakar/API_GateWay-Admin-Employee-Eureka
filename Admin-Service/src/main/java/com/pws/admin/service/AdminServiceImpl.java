package com.pws.admin.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pws.admin.dto.PermissionDTO;
import com.pws.admin.dto.SignUpDTO;
import com.pws.admin.dto.UpdatePasswordDTO;
import com.pws.admin.dto.UserBasicDetailsDTO;
import com.pws.admin.dto.UserRoleXrefDTO;
import com.pws.admin.entity.Module;
import com.pws.admin.entity.Permission;
import com.pws.admin.entity.Role;
import com.pws.admin.entity.Skill;
import com.pws.admin.entity.User;
import com.pws.admin.entity.UserRoleXref;
import com.pws.admin.exception.config.PWSException;
import com.pws.admin.repository.ModuleRepository;
import com.pws.admin.repository.PermissionRepository;
import com.pws.admin.repository.RoleRepository;
import com.pws.admin.repository.SkillRepository;
import com.pws.admin.repository.UserRepository;
import com.pws.admin.repository.UserRoleXrefRepository;
import com.pws.admin.utility.DateUtils;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private UserRoleXrefRepository userRoleXrefRepository;

	@Autowired
	private PermissionRepository permissionRepository;
	
	@Autowired
	private SkillRepository skillRepository;

	Refill refill = Refill.of(5, Duration.ofMinutes(1));
	private Bucket bucket = Bucket4j.builder().addLimit(Bandwidth.classic(5, refill)).build();

	@Override
	public void UserSignUp(SignUpDTO signupDTO) throws PWSException {

		Optional<User> optionalUser = userRepository.findUserByEmail(signupDTO.getEmail());
		if (optionalUser.isPresent())
			throw new PWSException("User Already Exist with Email : " + signupDTO.getEmail());
		User user = new User();
		user.setDateOfBirth(DateUtils.getUtilDateFromString(signupDTO.getDateOfBirth()));
		user.setFirstName(signupDTO.getFirstName());
		user.setIsActive(true);
		user.setLastName(signupDTO.getLastName());
		user.setEmail(signupDTO.getEmail());
		user.setPhoneNumber(signupDTO.getPhoneNumber());
		PasswordEncoder encoder = new BCryptPasswordEncoder(8);
		// Set new password
		user.setPassword(encoder.encode(signupDTO.getPassword()));

		userRepository.save(user);
	}

	@Override
	@CachePut(cacheNames = "User",key = "#user.id")
	public void updateUserPassword(UpdatePasswordDTO userPasswordDTO) throws PWSException {
		Optional<User> optionalUser = userRepository.findUserByEmail(userPasswordDTO.getUserEmail());

		PasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = null;
		if (!optionalUser.isPresent()) {
			throw new PWSException("User Not present ");
		}
		user = optionalUser.get();
		if (encoder.matches(userPasswordDTO.getOldPassword(), user.getPassword())) {
			if (userPasswordDTO.getNewPassword().equals(userPasswordDTO.getConfirmNewPassword())) {

				user.setPassword(encoder.encode(userPasswordDTO.getConfirmNewPassword()));
				userRepository.save(user);
			} else {
				throw new PWSException("new password and confirm password doesnot match ");
			}

		} else {
			throw new PWSException("oldpassword not matched");
		}

	}

	@Override
	public void addRole(Role role) throws PWSException {
		role.setIsActive(true);
		roleRepository.save(role);
	}

	@Override
	@CachePut(cacheNames = "Role", key="#role.id")
	public void updateRole(Role role) throws PWSException {
		System.out.println("fetched from DB");
		roleRepository.save(role);
	}

	@Override
	@Cacheable(cacheNames = "Role")
	public List<Role> fetchAllRole() throws PWSException {
		System.out.println("fetched from DB");
		return roleRepository.findAll();
	}

	@Override
	@Cacheable(cacheNames = "Role", key="#id")
	public Optional<Role> fetchRoleById(Integer id) throws PWSException {
		System.out.println("fetched from DB");
		return roleRepository.findById(id);
	}

	@Override
	@CachePut(cacheNames = "Role",key="#id")
	public void deactivateOrActivateRoleById(Integer id, boolean flag) throws PWSException {
		Optional<Role> role = roleRepository.findById(id);
		if (role.isPresent()) {
			role.get().setIsActive(flag);
			roleRepository.save(role.get());
		}
	}

	@Override
	public void addModule(Module module) throws PWSException {
		System.out.println("fetched from DB");
		moduleRepository.save(module);
	}

	@Override
	@CachePut(cacheNames = "Module", key="#module.id")
	public void updateModule(Module module) throws PWSException {
		Optional<Module> optionalModule = moduleRepository.findById(module.getId());
		if (optionalModule.isPresent()) {
			moduleRepository.save(module);
		} else
			throw new PWSException("Module Doest Exist");

	}

	@Override
	@Cacheable(cacheNames = "Module")
	public List<Module> fetchAllModule() throws PWSException {
		System.out.println("fetched from DB");
		return moduleRepository.findAll();
	}

	@Override
	@Cacheable(cacheNames = "Module", key="#id")
	public Optional<Module> fetchModuleById(Integer id) throws PWSException {
		System.out.println("fetched from DB");
		Optional<Module> optionalModule = moduleRepository.findById(id);
		if (optionalModule.isPresent()) {
			return optionalModule;
		} else
			throw new PWSException("Module Doest Exist");
	}

	@Override
	@CachePut(cacheNames = "Module",key="#id")
	public void deactivateOrActivateModuleById(Integer id, boolean flag) throws PWSException {
		Optional<Module> optionalModule = moduleRepository.findById(id);
		Module module = optionalModule.get();
		if (optionalModule.isPresent()) {
			module.setIsActive(flag);
			moduleRepository.save(module);
		} else
			throw new PWSException("Module Doest Exist");
	}

	@Override
	@CachePut(cacheNames = "UserRoleXref", key="#userRoleXrefDTO.id")
	public void saveOrUpdateUserXref(UserRoleXrefDTO userRoleXrefDTO) throws PWSException {
		Optional<UserRoleXref> optionalUserRoleXref = userRoleXrefRepository.findById(userRoleXrefDTO.getId());
		UserRoleXref userRoleXref = null;
		if (optionalUserRoleXref.isPresent()) {
			userRoleXref = optionalUserRoleXref.get();
		} else {
			userRoleXref = new UserRoleXref();
		}
		Optional<User> optionalUser = userRepository.findById(userRoleXrefDTO.getUserId());
		if (optionalUser.isPresent()) {
			userRoleXref.setUser(optionalUser.get());
		} else {
			throw new PWSException("User Doest Exist");
		}

		Optional<Role> optionalRole = roleRepository.findById(userRoleXrefDTO.getRoleId());
		if (optionalRole.isPresent()) {
			userRoleXref.setRole(optionalRole.get());
		} else {
			throw new PWSException("Role Doest Exist");
		}
		userRoleXref.setIsActive(userRoleXrefDTO.getIsActive());

		userRoleXrefRepository.save(userRoleXref);

	}

	@Override
	@CachePut(cacheNames = "UserRoleXref",key="#id")
	public void deactivateOrActivateAssignedRoleToUser(Integer id, Boolean flag) throws PWSException {
		Optional<UserRoleXref> optionalUserRoleXref = userRoleXrefRepository.findById(id);
		UserRoleXref userRoleXref = optionalUserRoleXref.get();
		if (optionalUserRoleXref.isPresent()) {
			optionalUserRoleXref.get().setIsActive(flag);
			userRoleXrefRepository.save(userRoleXref);
		} else
			throw new PWSException("Record Doest Exist");

	}

	@Override
	@Cacheable(cacheNames = "UserRoleXref", key="#id")
	public Optional<UserRoleXref> fetchUserById(Integer id) throws PWSException {
		System.out.println("fetched from DB");
		return userRoleXrefRepository.findById(id);

	}

	@Override
	@Cacheable(cacheNames = "UserRoleXref",key = "#roleId")
	public List<User> fetchUserByRole(Integer roleId) throws PWSException {
		System.out.println("fetched from DB");
		return userRoleXrefRepository.fetchAllUsersByRoleId(roleId);
	}

	@Override
	public void addPermission(PermissionDTO permissionDTO) throws PWSException {
		Permission permission = new Permission();

		permission.setIsActive(permissionDTO.getIsActive());
		permission.setIsAdd(permissionDTO.getIsAdd());
		permission.setIsDelete(permissionDTO.getIsDelete());
		permission.setIsUpdate(permissionDTO.getIsUpdate());
		permission.setIsView(permissionDTO.getIsView());
		Optional<Module> module = moduleRepository.findById(permissionDTO.getModule());
		permission.setModule(module.get());
		Optional<Role> role = roleRepository.findById(permissionDTO.getRole());
		permission.setRole(role.get());
		permissionRepository.save(permission);

	}

	@Override
	@CachePut(cacheNames = "Permission", key="#permission.id")
	public void updatePermission(PermissionDTO permissionDTO) throws PWSException {
		Optional<Permission> optionalpermission = permissionRepository.findById(permissionDTO.getId());
		if (optionalpermission.isPresent()) {
			optionalpermission.get().getId();
			optionalpermission.get().setIsActive(permissionDTO.getIsActive());
			optionalpermission.get().setIsAdd(permissionDTO.getIsAdd());
			optionalpermission.get().setIsDelete(permissionDTO.getIsDelete());
			optionalpermission.get().setIsUpdate(permissionDTO.getIsUpdate());
			optionalpermission.get().setIsView(permissionDTO.getIsView());
			Optional<Module> module = moduleRepository.findById(permissionDTO.getModule());
			optionalpermission.get().setModule(module.get());
			Optional<Role> role = roleRepository.findById(permissionDTO.getRole());
			optionalpermission.get().setRole(role.get());
			permissionRepository.save(optionalpermission.get());

		} else {
			throw new PWSException("Record Doest Exist");
		}

	}

	@Override
	@Cacheable(cacheNames = "Permission")
	public List<Permission> fetchAllPermission() throws PWSException {
		System.out.println("fetched from DB");
		List<Permission> permissionlist = permissionRepository.findAll();
		return permissionlist;
	}

	@Override
	@Cacheable(cacheNames = "Permission", key="#id")
	public Optional<Permission> fetchPermissionById(Integer id) throws PWSException {
		System.out.println("fetched from DB");
		Optional<Permission> optionalpermission = permissionRepository.findById(id);
		if (optionalpermission.isPresent()) {
			return optionalpermission;
		} else
			throw new PWSException("Permission Does't Exist");
	}

	@Override
	@CachePut(cacheNames = "Permission",key="#permission.id")
	public void deactivateOrActivatePermissionById(PermissionDTO permissionDTO) throws PWSException {
		Optional<Permission> optionalPermission = permissionRepository.findById(permissionDTO.getId());
		Permission permission = null;
		if (optionalPermission.isPresent()) {
			permission = optionalPermission.get();
			permission.setIsActive(permissionDTO.getIsActive());
			permissionRepository.save(permission);
		} else

			throw new PWSException("Permission Id Doest Exist");

	}

	@Override
	@Cacheable(cacheNames = "User",key = "#email")
	public UserBasicDetailsDTO getUserBasicInfoAfterLoginSuccess(String email) throws PWSException {
		System.out.println("fetched from DB");
	    Optional<User> optionalUser = userRepository.findUserByEmail(email);
	    if(! optionalUser.isPresent())
	        throw new PWSException("User Already Exist with Email : " + email);


	    User user = optionalUser.get();
	    UserBasicDetailsDTO userBasicDetailsDTO =new UserBasicDetailsDTO();
	    userBasicDetailsDTO.setUser(user);

	    List<Role> roleList = userRoleXrefRepository.findAllUserRoleByUserId(user.getId());
	    userBasicDetailsDTO.setRoleList(roleList);
	    List<Permission> permissionList =null;
	    if(roleList.size()>0)
	     permissionList = permissionRepository.getAllUserPermisonsByRoleId(roleList.get(0).getId());

	    userBasicDetailsDTO.setPermissionList(permissionList);
	    return userBasicDetailsDTO;
	}

	@Override
	public void addskill(Skill skill) throws PWSException {
		skill.setIsActive(true);
		skillRepository.save(skill);		
	}

	@Override
	@CachePut(cacheNames = "Skill", key="#skill.id")
	public void updateskill(Skill skill) throws PWSException {
		Optional<Skill> optionalskill= skillRepository.findById(skill.getId());
		if(optionalskill.isPresent()) {
		skillRepository.save(skill);
		}else
			throw new PWSException("Skill doesn't exist");
	}

	@Override
	@Cacheable(cacheNames = "Skill")
	public List<Skill> fetchAllSkills() throws PWSException {
		System.out.println("fetched from DB");
		return skillRepository.findAll();
	}

	@Override
	@Cacheable(cacheNames = "Role", key="#id")
	public Optional<Skill> fetchskillById(Integer id) throws PWSException {
		System.out.println("fetched from DB");
		Optional<Skill> optionalskill= skillRepository.findById(id);
		if(optionalskill.isPresent()) {
		return skillRepository.findById(id);
		}else
			throw new PWSException("Skill doesn't exist");
	}

	@Override
	@CacheEvict(cacheNames = "Skill",key = "#id")
	public void deleteskillById(Integer id) throws PWSException {
		skillRepository.deleteById(id);
	}

}
