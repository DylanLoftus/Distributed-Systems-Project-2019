package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

//This class is IMMUTABLE, it cannot be changed.
@XmlRootElement (name = "login")
public class Login {

//	@NotNull
	public int userId;
//	@NotNull
    public String password;
	
	public Login() {
		
	}

	public Login(int userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	@XmlElement
	@JsonProperty
	public int getUserId() {
		return userId;
	}

	@XmlElement
	@JsonProperty
	public String getPassword() {
		return password;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
