/**
 * File: Role.java
 * Mục đích: Thực thể đại diện cho bảng 'roles' trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.io.Serializable;

public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private int roleId;
    private String roleName;

    public Role() {
    }

    /**
     * Constructor khởi tạo đối tượng Role với mã vai trò và tên vai trò.
     */
    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
