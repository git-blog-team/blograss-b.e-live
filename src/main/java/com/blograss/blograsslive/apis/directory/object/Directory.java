package com.blograss.blograsslive.apis.directory.object;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.blograss.blograsslive.apis.auth.object.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "directory")
public class Directory {

    @Id
    @Column(name = "directoryid")
    private String directoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "userid")
    private User user;
}

