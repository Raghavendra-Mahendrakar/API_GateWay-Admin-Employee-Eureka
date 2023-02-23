package com.pws.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Login Request")
public class LoginDTO {
	@Schema(description = "Enter UserName",example = "abcd..",required = true)
	private String userName;

	@Schema(description = "Enter Password",example = "tes@1234..",required = true)
	private String password;

}
