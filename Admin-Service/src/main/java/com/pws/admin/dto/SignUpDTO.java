package com.pws.admin.dto;

import lombok.Data;

import javax.persistence.Column;
import java.sql.Date;

/**
 * @Author Vinayak M
 * @Date 09/01/23
 */
@Data
public class SignUpDTO {

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String email;

    private String phoneNumber;

    private String password;
}
