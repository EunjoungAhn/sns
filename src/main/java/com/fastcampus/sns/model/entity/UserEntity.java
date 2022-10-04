package com.fastcampus.sns.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class UserEntity {
    @Id
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "getPassword")
    private String password;

    public String getPassword() {
        return "";
    }

    public void setId(int i) {
    }

    public void setUserName(String userName) {
    }

    public void setPassword(String password) {
    }
}
