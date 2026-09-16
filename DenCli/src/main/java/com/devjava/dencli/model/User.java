/**
 * File: User.java
 * Mục đích: Thực thể đại diện cho bảng 'users' trong cơ sở dữ liệu.
 */
package com.devjava.dencli.model;

import java.sql.Date;
import java.sql.Timestamp;

public class User {

    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private int roleId;
    private String gender;
    private Date dob;
    private String address;
    private Timestamp createdAt;
    private int displayOrder;

    // Thuộc tính mở rộng để hiển thị tên vai trò mà không cần truy vấn bảng riêng
    private String roleName;

    public User() {
    }

    /**
     * Constructor khởi tạo đối tượng User với đầy đủ các trường dữ liệu từ cơ sở dữ liệu.
     */
    public User(int userId, String fullName, String email, String password, String phone, 
                int roleId, String gender, Date dob, String address, Timestamp createdAt, int displayOrder) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.roleId = roleId;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.createdAt = createdAt;
        this.displayOrder = displayOrder;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
