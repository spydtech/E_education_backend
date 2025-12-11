package com.Eonline.Education.response;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthResponse {

    private String jwt;

    private boolean status;
    private String role;

    public boolean isStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public AuthResponse() {
        // TODO Auto-generated constructor stub
    }

    public AuthResponse(String jwt, boolean status) {
        super();
        this.jwt = jwt;
        this.status = status;
    }
    public AuthResponse(String jwt, boolean status,String role) {
        super();
        this.jwt = jwt;
        this.status = status;
        this.role=role;
    }

    public AuthResponse(Object o, boolean b, String s) {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public void setEmail(String email) {
    }

    public void setFullName(String s) {

    }
}
