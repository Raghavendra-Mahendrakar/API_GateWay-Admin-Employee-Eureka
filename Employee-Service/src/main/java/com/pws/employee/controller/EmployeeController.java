package com.pws.employee.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pws.employee.utility.SwaggerLogsConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.pws.employee.ApiSuccess;
import com.pws.employee.dto.LoginDTO;
import com.pws.employee.dto.SignUpDTO;
import com.pws.employee.dto.UpdatePasswordDTO;
import com.pws.employee.dto.UserBasicDetailsDTO;
import com.pws.employee.dto.UserSkillXrefDTO;
import com.pws.employee.entity.Skill;
import com.pws.employee.entity.User;
import com.pws.employee.entity.UserSkillXref;
import com.pws.employee.exception.config.PWSException;
import com.pws.employee.service.EmployeeService;
import com.pws.employee.utility.CommonUtils;
import com.pws.employee.utility.JwtUtil;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
@CrossOrigin(origins = "http://domain2.com")
@RestController
@RequestMapping("/employee")

public class EmployeeController {


    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DiscoveryClient discoveryClient;

    public void getAdmin() throws RestClientException, IOException {

        List<ServiceInstance> instances = discoveryClient.getInstances("API-GATEWAY");
        ServiceInstance serviceInstance = instances.get(0);

        String baseUrl = serviceInstance.getUri().toString();

        baseUrl = baseUrl + "/admin/private/module/fetchall";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(baseUrl,
                    HttpMethod.GET, getHeaders(), String.class);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.out.println(response.getBody());
    }

    private static HttpEntity<?> getHeaders() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return new HttpEntity<>(headers);
    }

    @Operation(summary = "SignUp")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SignUp Successfull",
                    content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.SIGNUP_201_SUCCESS)}
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.SIGNUP_400_FAILURE)})}),
            @ApiResponse(responseCode = "404", description = "Invalid Credentials",
                    content = @Content)})
    @PostMapping("public/user/signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpDTO signUpDTO) throws PWSException {
        employeeService.UserSignUp(signUpDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.CREATED));
    }


    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated successfully", content = {
                    @Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.Authenticate_200_SUCCESS)})}),
            @ApiResponse(responseCode = "400", description = "Invalid UserName/Password supplied", content = {
                    @Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.Authenticate_400_Failure)})}),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content)})
    @PostMapping("/authenticate")
    public String generateToken(@RequestBody LoginDTO loginDTO) throws PWSException {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword())
            );
        } catch (Exception ex) {
            throw new PWSException("inavalid username/password");
        }
        return jwtUtil.generateToken(loginDTO.getUserName());
    }


    @Operation(summary = "Update user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password Updated Successfully",
                    content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.UPDATE_USER_PASSWORD_200_SUCCESS)}
                    )}),
            @ApiResponse(responseCode = "400", description = "Invalid UserName/Password supplied",
                    content = {@Content(mediaType = "application/json", examples = {@ExampleObject(value = SwaggerLogsConstants.UPDATE_USER_PASSWORD_400_FAILURE)}
                    )}),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @PutMapping("private/update/user/password")
    public ResponseEntity<Object> updateUserPassword(@RequestBody UpdatePasswordDTO userPasswordDTO) throws PWSException {
        employeeService.updateUserPassword(userPasswordDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }


    @Operation(summary = "User Details After Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Details fetched Successfully",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " User Not found",
                    content = @Content)})
    @GetMapping("private/userdetails")
    public ResponseEntity<Object> getUserBasicInfoAfterLoginSuccess(@Parameter(description = "Enter Email", example = "Aryaa@Gfmail.com", required = true) @RequestParam String email) throws PWSException {
        UserBasicDetailsDTO userBasicDetailsDTO = employeeService.getUserBasicInfoAfterLoginSuccess(email);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, userBasicDetailsDTO));
    }

    @Operation(summary = "refresh token when old token expired")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "refresh token Generated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "please enter a valid expired token",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "token not Generated",
                    content = {@Content(mediaType = "application/json")})})

    @GetMapping("private/token/refresh")
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring(7);
        Boolean isexp = jwtUtil.isTokenExpired(token);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && isexp != false) {
            return jwtUtil.refreshToken(token);
        } else {
            throw new PWSException("enter a expired valid token");
        }

    }


    @Operation(summary = "Fetch All Skill")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skills Fetched Successfully",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Skills Not found",
                    content = @Content)})
    @GetMapping("private/skill/fetchall/{offset}/{pageSize}/{field}")
    public ResponseEntity<Object> fetchAllSkills(@PathVariable int offset, @PathVariable int pageSize, @PathVariable String field) throws PWSException {
        Page<Skill> skilllist = employeeService.fetchAllSkills(offset, pageSize, field);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilllist));
    }


    @Operation(summary = "Fetch All User Skills Using Pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Skills Fetched Successfully",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User Skills Not found",
                    content = @Content)})
    @GetMapping("private/user/fetchall/skill")
    public ResponseEntity<Object> fetchAllUserSkills(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", required = false, defaultValue = Integer.MAX_VALUE + "") Integer size,
                                                     @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
                                                     @RequestParam(value = "order", required = false, defaultValue = "DESCENDING") String order,
                                                     @RequestParam Integer id) throws PWSException {
        Page<Skill> skilllist = employeeService.fetchAllUserSkills(page, size, sort, order, id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilllist));
    }

    @Operation(summary = "Fetch Skill By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill Fetched Successfully",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Skill Not found",
                    content = @Content)})
    @GetMapping("private/skill/fetchby/id")
    public ResponseEntity<Object> fetchAllSkillsByid(@RequestParam Integer id) throws PWSException {
        Optional<Skill> skillbyid = employeeService.fetchAllSkillsByid(id);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skillbyid));
    }


    @Operation(summary = "Fetch All Active Skills")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active Skills Fetched Successfully",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Active Skills Not found",
                    content = @Content)})
    @GetMapping("private/skill/fetch/allactive")
    public ResponseEntity<Object> fetchAllActiveSkills() throws PWSException {
        List<Skill> skilllist = employeeService.fetchAllActiveSkills();
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilllist));
    }


    @Operation(summary = "Fetch All Skills By Flag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " Skills Fetched Successfully",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Skills Not found",
                    content = @Content)})
    @GetMapping("private/skill/fetch/allinactive")
    public ResponseEntity<Object> fetchAllSkillsByFlag(@RequestParam Boolean flag) throws PWSException {
        List<Skill> skilllist = employeeService.fetchAllSkillsByFlag(flag);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK, skilllist));
    }


    @Operation(summary = "Add Skill To User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill Added Successfully",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Not found",
                    content = @Content)})
    @PostMapping("private/skill/add/to/user")
    public ResponseEntity<Object> addSkillToUser(@RequestBody UserSkillXrefDTO userSkillXrefDTO) throws PWSException {
        employeeService.addSkillToUser(userSkillXrefDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }


    @Operation(summary = "Save Or Update UserSkillXref")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Successfull",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Not found",
                    content = @Content)})
    @PostMapping("private/skill/saveorupdate")
    public ResponseEntity<Object> saveOrUpdateskilluserXref(@RequestBody UserSkillXrefDTO userSkillXrefDTO) throws PWSException {
        employeeService.saveOrUpdateskilluserXref(userSkillXrefDTO);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }

    @Operation(summary = "Activate or Deactivate Assigned Role To User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation Successfull",
                    content = {@Content(mediaType = "application/json"
                    )}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = " Not found",
                    content = @Content)})
    @PutMapping("private/skill/Activate/deactivate")
    public ResponseEntity<Object> deactivateOrActivateAssignedRoleToUser(@RequestParam Integer id, @RequestParam Boolean flag) throws PWSException {
        employeeService.deactivateOrActivateSkillUserXref(id, flag);
        return CommonUtils.buildResponseEntity(new ApiSuccess(HttpStatus.OK));
    }


}