/**
 * Purpose: Model class representing the 'users' table in SQL Server.
 * Created Date: 12/08/2026
 * Last Updated Date: 12/08/2026
 */
package com.mycompany.dencli.models;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public User() {
    }

    /**
     * Constructor khởi tạo đối tượng User với đầy đủ các thuộc tính.
     */
    public User(int userId, String fullName, String email, String password, String phone, int roleId, String gender, Date dob, String address, Timestamp createdAt, int displayOrder) {
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

    /**
     * Hàm ghi đè phương thức toString để hỗ trợ in thông tin người dùng.
     */
    @Override // Ghi đè phương thức toString từ lớp Object để xuất dữ liệu dạng chuỗi
    public String toString() {
        return "User{" + "userId=" + userId + ", fullName=" + fullName + ", email=" + email + ", roleId=" + roleId + '}';
    }
}
