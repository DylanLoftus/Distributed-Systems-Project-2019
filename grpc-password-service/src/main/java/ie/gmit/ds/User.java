package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;

// This class is IMMUTABLE, it cannot be changed.
public class User {
	
	private int userId;
    private String userName;
    private String email;
    private String password;
    private String hash;
    private String salt;

	// No arg constructor for deserialization
	public User() {
		
	}
	
	// For create and update
	public User(int userId, String userName, String email, String password) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	// For returning all users and specific user
	public User(int userId, String userName, String email, String hash, String salt) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.hash = hash;
		this.salt = salt;
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
	
	@JsonProperty
	public String getHash() {
		return hash;
	}
	
	@JsonProperty
	public String getSalt() {
		return salt;
	}
}
