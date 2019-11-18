package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
	
	private int id;
	private String password;
	
	public Login() {
	}

	public Login(int id, String password) {
		this.id = id;
		this.password = password;
	}

	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonProperty
	public String getPassword() {
		return password;
	}
}
