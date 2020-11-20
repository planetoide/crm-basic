package com.work.main.models;

public class AuthenticationResponse {

    private String token;
    private Boolean isAdmin;

    public AuthenticationResponse() {

    }

    public AuthenticationResponse(String token, Boolean isAdmin) {
        this.token = token;
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
