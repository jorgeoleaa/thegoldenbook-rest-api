package com.thegoldenbook.rest.api.dto;

public class UserCredentials {
	
	private String email;
	private String password;
	
	public UserCredentials() {
		
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
	
}
