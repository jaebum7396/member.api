package com.member.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class MemberEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty(value="gildong0824", required=true)
    @Column(unique = true)
    private String userId;
    @ApiModelProperty(value="0824", required=true)
    @Column(length = 300, nullable = false)
    private String password;
    @ApiModelProperty(value="홍길동", required=true)
    private String name;
    @ApiModelProperty(value="860824-1655068", required=true)
    private String regNo;

    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<AuthEntity> roles = new ArrayList<>();

    public void setRoles(List<AuthEntity> role) {
        this.roles = role;
        role.forEach(o -> o.setMemberEntity(this));
    }
}