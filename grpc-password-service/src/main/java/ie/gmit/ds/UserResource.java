package ie.gmit.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

	private HashMap<Integer, User> userMap = new HashMap<>();

	PasswordClient client = new PasswordClient("localhost", 50551);
	
	public UserResource() {
		User testUser = new User(1, "Lala", "lala@gmail.com", "super");
		userMap.put(testUser.getUserId(), testUser);
	}
	
	@GET
	public List<User> getUsers() {
	    return new ArrayList<User>(userMap.values());
	}
	
	@POST
	public void addUser(User user) {
		client.makePassword(user);
		User newUser = new User(user.getUserId(), user.getUserName(), user.getEmail(), user.getHash(), user.getSalt());
		userMap.put(newUser.getUserId(), newUser);
	}
	
	@Path("/{userId}")
	@GET
	public User getUserById(@PathParam("userId") int id) {
		
		return userMap.get(id);
	}
	
	@Path("/{userId}")
	@DELETE
	public List<User> deleteUser(@PathParam("userId") int id){
		userMap.remove(id);
		return (List<User>) userMap.values();
	}
	
	@Path("/login")
	@POST
	public Boolean loginUser(){
		return null;
	}

	
}
