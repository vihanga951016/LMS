package com.vp.lms.beans.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "institute")
public class InstituteBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String instituteShortName;
    private String address;
    private Integer subscription;
    private String email;
    private String contact;
    private boolean deactivated;
    private boolean notAvailable;

    public InstituteBean(Integer id) {
        this.id = id;
    }

    public InstituteBean(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
