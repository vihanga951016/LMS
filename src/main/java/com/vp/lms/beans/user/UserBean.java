package com.vp.lms.beans.user;

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
    private boolean accountIsActive;
    private boolean accountIsExpired;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    @OneToOne
    @Column(name = "userRole")
    private UserRoleBean userRoleBean;

    public UserBean(Integer id) {
        this.id = id;
    }
}
