package com.mycompany.blogmanagement.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;
    
    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;
    
    @OneToMany(mappedBy = "role")
    private Set<User> users;

    public Role() {}

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
}
