package com.example.cinemabookingsystemfe.data.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * LoginResponse - Response from POST /api/auth/login
 * Backend returns: token, refreshToken, expiresAt, user{nested}
 */
public class LoginResponse {
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("refreshToken")
    private String refreshToken;
    
    @SerializedName("expiresAt")
    private String expiresAt;
    
    @SerializedName("tokenExpiration")  // Alternative field name
    private String tokenExpiration;
    
    @SerializedName("user")
    private UserData user;

    public LoginResponse() {}
    
    // Nested User class
    public static class UserData {
        @SerializedName("userId")
        private int userId;
        
        @SerializedName("fullName")
        private String fullName;
        
        @SerializedName("email")
        private String email;
        
        @SerializedName("phoneNumber")
        private String phoneNumber;
        
        @SerializedName("roleId")
        private int roleId;
        
        @SerializedName("roleName")
        private String roleName;
        
        // Getters
        public int getUserId() { return userId; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
        public int getRoleId() { return roleId; }
        public String getRoleName() { return roleName; }
        
        // Setters (for mock/testing)
        public void setUserId(int userId) { this.userId = userId; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public void setEmail(String email) { this.email = email; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public void setRoleId(int roleId) { this.roleId = roleId; }
        public void setRoleName(String roleName) { this.roleName = roleName; }
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getExpiresAt() { 
        return expiresAt != null ? expiresAt : tokenExpiration; 
    }
    public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
    
    public String getTokenExpiration() { 
        return tokenExpiration != null ? tokenExpiration : expiresAt; 
    }
    public void setTokenExpiration(String tokenExpiration) { this.tokenExpiration = tokenExpiration; }
    
    public UserData getUser() { return user; }
    public void setUser(UserData user) { this.user = user; }
    
    // Convenience methods for backward compatibility
    public int getUserId() { return user != null ? user.getUserId() : 0; }
    public String getFullname() { return user != null ? user.getFullName() : null; }
    public String getEmail() { return user != null ? user.getEmail() : null; }
    public String getPhone() { return user != null ? user.getPhoneNumber() : null; }
    public String getRoleName() { return user != null ? user.getRoleName() : null; }
}
