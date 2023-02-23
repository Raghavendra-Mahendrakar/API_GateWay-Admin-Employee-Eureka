package com.pws.employee.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Update Password Request")

public class UpdatePasswordDTO {
	@Schema(description = "Enter User Email",example = "Arya@gfmail.com",required = true)
	private String userEmail;

	@Schema(description = "Enter Old Password",example = "Arya@123",required = true)
	private String oldPassword;

	@Schema(description = "Enter New Password",example = "Arya@gf123",required = true)
	private String newPassword;
	
	private String confirmNewPassword;
}