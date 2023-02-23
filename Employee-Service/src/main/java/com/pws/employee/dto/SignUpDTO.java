package com.pws.employee.dto;

import com.pws.employee.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
@Data
@Schema(description = "SignUp Request")

public class SignUpDTO {

    @Schema(description = "Enter FirstName",example = "Arya",required = true)
    private String firstName;
    @Schema(description = "Enter LastName",example = "Raj",required = true)
    private String lastName;
    @Schema(description = "Enter DateOfBirth",example = "12/02/2000",required = true)
    private String dateOfBirth;
    @Schema(description = "Enter Email",example = "Arya@gfmail.com",required = true)
    private String email;
    @Schema(description = "Enter PhoneNumber",example = "08020415566",required = true)
    private String phoneNumber;
    @Schema(description = "Enter Password",example = "arya@123",required = true)
    private String password;

    @Schema(description = "Enter RoleName",example = "Employee",required = true)
    private String roleName;
}