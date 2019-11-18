package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;

// This class is IMMUTABLE, it cannot be changed.
@XmlRootElement (name = "Employee")
public class User {
	@NotNull
	private int userId;
	@NotNull
    private String userName;
	@NotNull
    private String email;
	@NotNull
    private String password;
	@NotNull
    private String hash;
	@NotNull
    private String salt;

	// No arg constructor for deserialization
	public User() {
		
	}
	
	// For create and update
	public User(int userId, String userName, String email, String password) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	// For returning all users and specific user
	public User(int userId, String userName, String email, String hash, String salt) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.hash = hash;
		this.salt = salt;
	}

	@XmlElement
	@JsonProperty
	public int getUserId() {
		return userId;
	}

	@XmlElement
	@JsonProperty
	public String getUserName() {
		return userName;
	}

	@XmlElement
	@JsonProperty
	public String getEmail() {
		return email;
	}

	@XmlElement
	@JsonProperty
	public String getPassword() {
		return password;
	}
	
	@XmlElement
	@JsonProperty
	public String getHash() {
		return hash;
	}
	
	@XmlElement
	@JsonProperty
	public String getSalt() {
		return salt;
	}
}
