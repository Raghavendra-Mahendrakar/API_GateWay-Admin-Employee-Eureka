package com.pws.employee.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pws.employee.dto.SignUpDTO;
import com.pws.employee.dto.UpdatePasswordDTO;
import com.pws.employee.dto.UserBasicDetailsDTO;
import com.pws.employee.dto.UserSkillXrefDTO;
import com.pws.employee.entity.Permission;
import com.pws.employee.entity.Role;
import com.pws.employee.entity.Skill;
import com.pws.employee.entity.User;
import com.pws.employee.entity.UserRoleXref;
import com.pws.employee.entity.UserSkillXref;
import com.pws.employee.exception.config.PWSException;
import com.pws.employee.repository.PermissionRepository;
import com.pws.employee.repository.RoleRepository;
import com.pws.employee.repository.SkillRepository;
import com.pws.employee.repository.UserRepository;
import com.pws.employee.repository.UserRoleXrefRepository;
import com.pws.employee.repository.UserSkillXrefRepository;
import com.pws.employee.utility.CommonUtils;
import com.pws.employee.utility.DateUtils;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	

	@Autowired
	private SkillRepository skillRepository;
	
	
	@Autowired
	private UserRoleXrefRepository userRoleXrefRepository;

	@Autowired
	private UserSkillXrefRepository userSkillXrefRepository;

	@Autowired
	private PermissionRepository permissionRepository;

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

		Optional<Role> optionalRole = roleRepository.findByRolename(signupDTO.getRoleName());
		Role role = optionalRole.get();
		UserRoleXref userRole= new UserRoleXref();
		userRole.setRole(role);
		userRole.setUser(user);
		userRole.setIsActive(true);
		userRoleXrefRepository.save(userRole);


	}

	@Override
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
	public UserBasicDetailsDTO getUserBasicInfoAfterLoginSuccess(String email) throws PWSException {
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
	    List<Skill> skilllist = userSkillXrefRepository.fetchuserSkillsByid(user.getId());
	    userBasicDetailsDTO.setSkilllist(skilllist);

	    
	    return userBasicDetailsDTO;
	}

	@Override
	public Page<Skill> fetchAllSkills(int offset,int pageSize, String field)throws PWSException {
        return skillRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
    }
    
	@Override
	public Page<Skill> fetchAllUserSkills(int page,int pageSize, String sort, String order, Integer id)throws PWSException {
		Pageable pageable=CommonUtils.getPageable(page, pageSize, sort, order);
        return userSkillXrefRepository.fetchAllUserSkills(pageable,id);
    }
	
    @Override
	@Cacheable(cacheNames = "Skill", key = "#id")
    public Optional<Skill> fetchAllSkillsByid(Integer id) throws PWSException {
        Optional<Skill> skillbyid = skillRepository.findById(id);
        if (skillbyid.isPresent()) {
        return    skillRepository.findById(id);
        }
        else {
            throw new PWSException("Id not found");
        }
    }
    
    @Override
	@Cacheable(cacheNames = "Skill")
	public List<Skill> fetchAllActiveSkills() throws PWSException {
        return  userSkillXrefRepository.fetchAllActiveSkills();
        }
        
    @Override
	@Cacheable(cacheNames = "Skill", key = "#flag")
	public List<Skill> fetchAllSkillsByFlag(Boolean flag) throws PWSException {
        return  userSkillXrefRepository.fetchAllSkillsByFlag(flag);
        }
    
    
    @Override
    public void addSkillToUser(UserSkillXrefDTO userSkillXrefDTO) throws PWSException {
        UserSkillXref userSkillXref = new UserSkillXref();
        Optional<User> optionalUser = userRepository.findById(userSkillXrefDTO.getUserId());
        if (optionalUser.isPresent()) {
            userSkillXref.setUser(optionalUser.get());
        } else {
            throw new PWSException("User Doest Exist");
        }
        Optional<Skill> optionalskill = skillRepository.findById(userSkillXrefDTO.getSkillId());
        if (optionalskill.isPresent()) {
            userSkillXref.setSkill(optionalskill.get());
        } else {
            throw new PWSException("skill Doest Exist");
        }
        
        userSkillXref.setProficiencyevel(userSkillXrefDTO.getSkilllevel());
        userSkillXref.setIsActive(userSkillXrefDTO.getIsActive());

        userSkillXrefRepository.save(userSkillXref);
    }

    
    @Override
	@CachePut(cacheNames = "UserSkillXref")
    public void saveOrUpdateskilluserXref(UserSkillXrefDTO userSkillXrefDTO) throws PWSException {
        Optional<UserSkillXref> optskill= userSkillXrefRepository.findById(userSkillXrefDTO.getId());
        UserSkillXref userSkillXref = null;
        if(optskill.isPresent()) {
        	userSkillXref=optskill.get();
        }else {
        	userSkillXref=new UserSkillXref();
        }
        Optional<User> optionalUser = userRepository.findById(userSkillXrefDTO.getUserId());
        if (optionalUser.isPresent()) {
        	userSkillXref.setUser(optionalUser.get());
        } else {
            throw new PWSException("User Doest Exist");
        }
        Optional<Skill> optionalskill = skillRepository.findById(userSkillXrefDTO.getSkillId());
        if (optionalskill.isPresent()) {
        	userSkillXref.setSkill(optionalskill.get());
        } else {
            throw new PWSException("skill Doest Exist");
        }
        
         userSkillXref.setProficiencyevel(userSkillXrefDTO.getSkilllevel());
         userSkillXref.setIsActive(userSkillXrefDTO.getIsActive());
         

        
        userSkillXrefRepository.save(userSkillXref);
    }
    
    @Override
	@CachePut(cacheNames = "UserSkillXref",key = "#id")
    public void deactivateOrActivateSkillUserXref(Integer id, Boolean flag) throws PWSException {
        Optional<UserSkillXref> optionalUserRoleXref = userSkillXrefRepository.findById(id);
        UserSkillXref userSkillXref = optionalUserRoleXref.get();
        if (optionalUserRoleXref.isPresent()) {
        	userSkillXref.setIsActive(flag);
            userSkillXrefRepository.save(userSkillXref);
        } else
            throw new PWSException("data Doest Exist");
    }
    
    


}
