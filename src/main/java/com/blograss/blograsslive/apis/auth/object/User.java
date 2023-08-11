package com.blograss.blograsslive.apis.auth.object;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@Table(name = "user")
public class User {

    @Id
    @Column(name = "userid")
    private String userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "blogusername")
    private String blogUserName;

    @Column(name = "reportcount")
    private Integer reportCount;

}

