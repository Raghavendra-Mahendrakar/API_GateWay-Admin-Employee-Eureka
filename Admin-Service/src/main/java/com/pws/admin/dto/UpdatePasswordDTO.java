package com.pws.admin.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {
	
	private String userEmail;
	
	private String oldPassword;
	
	private String newPassword;
	
	private String confirmNewPassword;
}