package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	
	private int userId;
    private String userName;
    private String email;
    private String password;
	
	// No arg constructor for deserialization
	public User() {
		
	}

	public User(int userId, String userName, String email, String password) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	@JsonProperty
	public int getUserId() {
		return userId;
	}

	@JsonProperty
	public String getUserName() {
		return userName;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public String getPassword() {
		return password;
	}
}
