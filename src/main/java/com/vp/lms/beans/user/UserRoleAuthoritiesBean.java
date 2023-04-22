package com.vp.lms.beans.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_role_authorities")
public class UserRoleAuthoritiesBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "role")
    private UserRoleBean role;
    @OneToOne
    @JoinColumn(name = "authorities")
    private UserAuthoritiesBean authorities;
}
