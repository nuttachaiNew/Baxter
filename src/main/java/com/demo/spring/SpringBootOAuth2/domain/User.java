package com.demo.spring.SpringBootOAuth2.domain;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
@Entity
@Data
@Table(name = "app_user")
public class User extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    private Long version;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private String address;
    private String image;
    private String status;
   
    @OneToOne(fetch = FetchType.LAZY)
    private Branch branch;

    @OneToOne(fetch = FetchType.LAZY)
    private Role role;

    /**
     * Roles are being eagerly loaded here because
     * they are a fairly small collection of items for this example.
     */
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "userRole", joinColumns = {
//            @JoinColumn(name = "user_id", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = false)})
//    private Set<Role> roles = new HashSet<Role>();
//
//    public Set<Role> getAuthorities() {
//        return this.roles;
//    }




    // public Long getId() {
    //     return id;
    // }

    // public void setId(Long id) {
    //     this.id = id;
    // }

    // public String getUsername() {
    //     return username;
    // }

    // public void setUsername(String username) {
    //     this.username = username;
    // }

    // public String getPassword() {
    //     return password;
    // }

    // public void setPassword(String password) {
    //     this.password = password;
    // }

    // public String getFirstName() {
    //     return firstName;
    // }

    // public void setFirstName(String firstName) {
    //     this.firstName = firstName;
    // }

    // public String getLastName() {
    //     return lastName;
    // }

    // public void setLastName(String lastName) {
    //     this.lastName = lastName;
    // }

    // public List<Role> getRoles() {
    //     return roles;
    // }

    // public void setRoles(List<Role> roles) {
    //     this.roles = roles;
    // }
}