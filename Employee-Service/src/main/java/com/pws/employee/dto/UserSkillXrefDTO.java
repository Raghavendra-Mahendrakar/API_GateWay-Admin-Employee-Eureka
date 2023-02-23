package com.pws.employee.dto;

import com.pws.employee.entity.UserSkillXref.Keyword;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Add Skill To User Request")

public class UserSkillXrefDTO {
    @Schema(description = "Enter Id",example = "111",required = true)
    private Integer id;
    @Schema(description = "Enter User Id",example = "111",required = true)
    private Integer userId;
    @Schema(description = "Enter Skill Id",example = "66",required = true)
    private Integer skillId;
    @Schema(description = "Enter Skill Level",example = "Beginner/Intermediate/Expert",required = true)
    private Keyword skilllevel;

    @Schema(description = "Enter IsActive",example = "true/false",required = true)
    private Boolean isActive;
}
