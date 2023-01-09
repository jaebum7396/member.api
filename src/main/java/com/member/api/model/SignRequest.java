package com.member.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignRequest {
    @ApiModelProperty(value="userId", example="gildong0824", required=true)
    private String userId;
    @ApiModelProperty(value="password", example="1234", required=true)
    private String password;
    @ApiModelProperty(value="name", example="홍길동", required=true)
    private String name;
    @ApiModelProperty(value="regNo", example="860824-1655068", required=true)
    private String regNo;
}