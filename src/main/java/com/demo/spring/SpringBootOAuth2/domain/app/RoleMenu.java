package com.demo.spring.SpringBootOAuth2.domain.app;

import com.demo.spring.SpringBootOAuth2.domain.general.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="ROLE_MENU")
public class RoleMenu extends BaseModel {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
   
    @JsonIgnore
    private Long version;

     /**
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role")
    private Role role;

    /**
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu")
    private Menu menu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

  
}