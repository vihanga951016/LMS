package com.vp.lms.beans.user;

import com.vp.lms.beans.property.InstituteBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserBean implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Date dob;
    private String nic;
    private String address;
    private String phone;
    private String email;
    private String password;
    private boolean accountIsDeactivated;
    private boolean accountIsExpired;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    @OneToOne
    @JoinColumn(name = "userRole")
    private UserRoleBean userRoleBean;
    @OneToOne
    @JoinColumn(name = "institute")
    private InstituteBean instituteBean;
    private String jwtType;

    @Transient
    private String token;

    public UserBean(Integer id) {
        this.id = id;
    }
}
