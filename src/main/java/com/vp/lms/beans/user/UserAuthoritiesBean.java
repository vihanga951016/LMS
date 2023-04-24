package com.vp.lms.beans.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorities")
public class UserAuthoritiesBean implements Serializable {

    @Id
    private String authority;
    private boolean disabled;
    private boolean deleted;
}
