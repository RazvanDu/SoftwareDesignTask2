package com.RazvanDu.project.model;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "sdassigment2", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")})
public class User {
    private Integer id;
    private String name;
    private String hash;
    private String email;
    private Integer type;
    private Integer newAdmin;

    public User(String name, String hash, String email, Integer type) {

        this.name = name;
        this.hash = hash;
        this.email = email;
        this.type = type;

    }

    public User() {

    }

    @Id
    @Basic
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Basic
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNewAdmin() {
        return newAdmin;
    }

    public void setNewAdmin(Integer newAdmin) {
        this.newAdmin = newAdmin;
    }

}
