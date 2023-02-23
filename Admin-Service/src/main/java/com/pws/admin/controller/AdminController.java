package com.pws.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.pws.admin.entity.*;
import com.pws.admin.entity.Module;
import com.pws.admin.utility.SwaggerLogsConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pws.admin.ApiSuccess;
import com.pws.admin.dto.LoginDTO;
import com.pws.admin.dto.PermissionDTO;
import com.pws.admin.dto.SignUpDTO;
import com.pws.admin.dto.UpdatePasswordDTO;
import com.pws.admin.dto.UserBasicDetailsDTO;
import com.pws.admin.dto.UserRoleXrefDTO;
import com.pws.admin.exception.config.PWSException;
import com.pws.admin.service.AdminService;
import com.pws.admin.utility.CommonUtils;
import com.pws.admin.utility.JwtUtil;


/**
 * @Author Vinayak M
 * @Date 09/01/23
 */

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private JwtUtil jwtUtil;
	  @Autowired
	    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

    @Operation(summary = "SignUp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SignUp successfully", content = {
                    @Content(mediaType = "application/json",examples = {@ExampleObject(value = SwaggerLogsConstants.SIGNUP_201_SUCCESS)}) }),
            @ApiResponse(responseCode = "400", description = "User Already Exist with Email", content = {
                    @Content(mediaType = "application/json",examples = {@ExampleObject(value = SwaggerLogsConstants.SIGNUP_400_FAILURE)})}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content) })
    @PostMapping("public/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpDTO signUpDTO) throws PWSException {
        adminService.UserSignUp(signUpDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.CREATED));
    }

    @Operation(summary = "Authenticate User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated successfully", content = {
                    @Content(mediaType = "application/json",examples = {@ExampleObject(value = SwaggerLogsConstants.Authenticate_200_SUCCESS)}) }),
            @ApiResponse(responseCode = "400", description = "Invalid UserName/Password supplied", content = {
                    @Content(mediaType = "application/json",examples = {@ExampleObject(value = SwaggerLogsConstants.Authenticate_400_Failure)})}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content) })
	@PostMapping("/authenticate")
	public String generateToken(@RequestBody LoginDTO loginDTO) throws Exception {
		try {

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserName(),loginDTO.getPassword())
					);
		} catch (Exception ex) {
			throw new Exception("inavalid username/password");
		}
		return jwtUtil.generateToken(loginDTO.getUserName());
	}

    @Operation(summary = "Fetch All Skill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skills Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Skills Not found",
                    content = @Content) })
    @GetMapping("private/skill/fetch/all")
    public ResponseEntity<Object> fetchAllSkills() throws PWSException {
        List<Skill> skillList = adminService.fetchAllSkills();
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skillList));
    }

    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password Updated successfully", content = {
                    @Content(mediaType = "application/json",examples = {@ExampleObject(value = SwaggerLogsConstants.UPDATE_USER_PASSWORD_200_SUCCESS)}) }),
            @ApiResponse(responseCode = "400", description = "new password and confirm password does not match", content = {
                    @Content(mediaType = "application/json",examples = {@ExampleObject(value = SwaggerLogsConstants.UPDATE_USER_PASSWORD_400_FAILURE)})}),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content) })
	@PutMapping("private/update/user/password")
	public ResponseEntity<Object> updateUserPassword(@RequestBody UpdatePasswordDTO userPasswordDTO)throws PWSException{
		adminService.updateUserPassword(userPasswordDTO);
		return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
	}

    @Operation(summary = "Add New Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Role Added Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @PostMapping("private/role/add")
    public ResponseEntity<Object> addRole(@RequestBody Role role) throws PWSException {
        adminService.addRole(role);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Update Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " Role Updated Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Role Not found",
                    content = @Content) })
    @PutMapping ("private/role/update")
    public ResponseEntity<Object> updateRole(@RequestBody Role role) throws PWSException {
        adminService.updateRole(role);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }
    @Operation(summary = "Fetch Role By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " Role Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Role Not found",
                    content = @Content) })
    @GetMapping ("private/role/fetch/by/id")
    public ResponseEntity<Object> fetchRoleById( @RequestParam Integer id) throws PWSException {
        Optional<Role> role = adminService.fetchRoleById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, role.get()));
    }

    @Operation(summary = "Fetch All Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Role Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No Roles found",
                    content = @Content) })

    @GetMapping("private/role/fetch/all")
    public ResponseEntity<Object> fetchAllRole() throws PWSException {
        List<Role> roleList = adminService.fetchAllRole();
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, roleList));
    }

    @Operation(summary = "Deactivate Or Activate Role ById")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Successfull",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Id Not found",
                    content = @Content) })
    @PostMapping("private/role/activate/deactivate")
    public ResponseEntity<Object> deactivateOrActivateRoleById(@RequestParam Integer id, @RequestParam Boolean flag) throws PWSException {
        adminService.deactivateOrActivateRoleById(id, flag);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Add Module")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Model added Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @PostMapping("private/module/add")
    public ResponseEntity<Object> addModule(@RequestBody Module module) throws PWSException {
        adminService.addModule(module);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Update Module")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module updated Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Module Not found",
                    content = @Content) })
    @PutMapping("private/module/update")
    public ResponseEntity<Object> updateModule(@RequestBody Module module) throws PWSException {
        adminService.updateModule(module);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Fetch All Module")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All Modules Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Modules Not found",
                    content = @Content) })
    @GetMapping("private/module/fetchall")
    public ResponseEntity<Object> fetchAllModule() throws PWSException {
        List<Module> modulelist = adminService.fetchAllModule();
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, modulelist));
    }

    @Operation(summary = "Fetch Module By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Module Not found",
                    content = @Content) })
    @GetMapping("private/module/fetch/id")
    public ResponseEntity<Object> fetchModuleById(@RequestParam Integer id) throws PWSException {
        Optional<Module> module= adminService.fetchModuleById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, module));
    }

    @Operation(summary = "Activate or Deactivate Module")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " Operation Successfull",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Module Not found",
                    content = @Content) })
    @PostMapping("private/module/activate/inactivate")
    public ResponseEntity<Object> deactivateOrActivateModuleById(@RequestParam  Integer id,@RequestParam Boolean flag) throws PWSException {
        adminService.deactivateOrActivateModuleById(id,flag);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Save Or Update UserXref")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation  Successfull",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @PostMapping("private/save/update/userxref")
    public ResponseEntity<Object> saveOrUpdateUserXref(@RequestBody  UserRoleXrefDTO userRoleXrefDTO) throws PWSException {
        adminService.saveOrUpdateUserXref(userRoleXrefDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Activate or Deactivate Assigned Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " Successfull",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Not found",
                    content = @Content) })
    @PostMapping("private/userxref/activate/deactivate/byuser")
    public ResponseEntity<Object> deactivateOrActivateAssignedRoleToUser(@RequestParam Integer id, @RequestParam Boolean flag) throws PWSException {
        adminService.deactivateOrActivateAssignedRoleToUser(id, flag);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }


    @Operation(summary = "Fetch User By Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User Not found",
                    content = @Content) })
    @GetMapping("private/fetch/userbyrole")
    public ResponseEntity<Object> fetchUserByRole(@RequestParam Integer roleId) throws PWSException {
        List<User> user = adminService.fetchUserByRole(roleId);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, user));
    }

    @Operation(summary = "Fetch User By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User Not found",
                    content = @Content) })
    @GetMapping("private/fetch/fetchUserById")
    public ResponseEntity<Object> fetchUserById(@RequestParam Integer id) throws PWSException {
    	Optional<UserRoleXref> userRoleXrefs=adminService.fetchUserById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK,userRoleXrefs));
    }


    @Operation(summary = "Add Permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission Added Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Not found",
                    content = @Content) })
    @PostMapping("private/permmision/add")
    public ResponseEntity<Object> addPermission(@RequestBody PermissionDTO permissionDTO) throws PWSException {
        adminService.addPermission(permissionDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Update Permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission Updated Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Permission Not found",
                    content = @Content) })
    @PutMapping("private/permmision/update")
    public ResponseEntity<Object> updatePermission(@RequestBody PermissionDTO permissionDTO) throws PWSException {
        adminService.updatePermission(permissionDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Fetch All Permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Permissions Not found",
                    content = @Content) })
    @GetMapping("private/permission/fetchall")
    public ResponseEntity<Object> fetchAllPermission() throws PWSException {
    	 List<Permission> permissionlist = adminService.fetchAllPermission();
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, permissionlist ));
    }

    @Operation(summary = "Fetch Permission By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Permission Not found",
                    content = @Content) })
    @GetMapping("private/permission/fetchPermission/Id")
    public ResponseEntity<Object> fetchPermissionById(@RequestParam Integer id) throws PWSException {
    	Optional<Permission> optionalpermission = adminService.fetchPermissionById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, optionalpermission));
    }

    @Operation(summary = "Activate or Deactivate Permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Successfull",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Permission Not found",
                    content = @Content) })
    @PutMapping("private/permission/activate/deactivate/byuser")
    public ResponseEntity<Object> deactivateOrActivatePermissionById(@RequestBody PermissionDTO permissionDTO) throws PWSException {
        adminService.deactivateOrActivatePermissionById(permissionDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, permissionDTO));
    }


    @Operation(summary = "User Details After Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Details fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " User Not found",
                    content = @Content) })
    @GetMapping("private/userdetails")
    public ResponseEntity<Object> getUserBasicInfoAfterLoginSuccess(@RequestParam  String email) throws PWSException{
        UserBasicDetailsDTO userBasicDetailsDTO = adminService.getUserBasicInfoAfterLoginSuccess(email);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, userBasicDetailsDTO));
    }

    @Operation(summary = "Add Skill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill Added Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Not found",
                    content = @Content) })
    @PostMapping("private/skill/add")
    public ResponseEntity<Object> addskill(@RequestBody Skill skill) throws PWSException {
        adminService.addskill(skill);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Update Skill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill Updated Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Skill Not found",
                    content = @Content) })
    @PutMapping ("private/skill/update")
    public ResponseEntity<Object> updateskill(@RequestBody Skill skill) throws PWSException {
        adminService.updateskill(skill);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Fetch Skill By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill Fetched Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Skill Not found",
                    content = @Content) })

    @GetMapping ("private/skill/fetch/by/id")
    public ResponseEntity<Object> fetchskillById( @RequestParam Integer id) throws PWSException {
        Optional<Skill> skill = adminService.fetchskillById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skill.get()));
    }

    @Operation(summary = "Delete Skill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill Deleted Successfully",
                    content = { @Content(mediaType = "application/json"
                    ) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Skill Not found",
                    content = @Content) })
    @DeleteMapping ("private/skill/delete/by/id")
    public ResponseEntity<Object> deleteskillById(@RequestParam Integer id) throws PWSException {
     adminService.deleteskillById(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }
    
    
}