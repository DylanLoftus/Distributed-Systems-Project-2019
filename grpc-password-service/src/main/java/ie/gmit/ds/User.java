package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;

// This class is IMMUTABLE, it cannot be changed.
@XmlRootElement (name = "user")
public class User {
	@NotNull
	private int userId;
	@NotNull
    private String userName;
	@NotNull
    private String email;
	@NotNull
    private String password;

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
	
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
